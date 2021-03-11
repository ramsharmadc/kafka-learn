package simpleapps.kafkapoc.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import simpleapps.kafkapoc.CustomSerDes;
import simpleapps.kafkapoc.Customer;
import simpleapps.kafkapoc.CustomerEmployeeComposition;
import simpleapps.kafkapoc.Employee;

import java.util.Properties;

public class KafkaStreamsExample {

    public void consume() {

        final KafkaStreams streams = getOutputStream();

        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    public static KafkaStreams getOutputStream() {

        final Properties streamsConfiguration = new Properties();

        // Give the Streams application a unique name.
        // The name must be unique in the Kafka cluster against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "global-tables-example");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "global-tables-example-client");

        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams-global-tables");

        // Set to earliest so we don't miss any data that arrived in the topics before the process
        // started
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put("default.deserialization.exception.handler", LogAndContinueExceptionHandler.class);

        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, Employee> employeeStream = builder.
                stream("my-topic", Consumed.with(Serdes.String(), CustomSerDes.employeeSerde()));

        final GlobalKTable<String, Customer>
                customers = builder.globalTable("topic-customer",
                Materialized.<String, Customer, KeyValueStore<Bytes, byte[]>>as("customer-store")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerDes.customerSerde()));

        final KStream<String, CustomerEmployeeComposition> customerOrdersStream = employeeStream.join(customers,
                new KeyValueMapper<String, Employee, String>() {
                    @Override
                    public String apply(String orderId, Employee order) {
                        System.out.println("Joining on " + orderId);
                        return String.valueOf(orderId);
                    }
                },
                new ValueJoiner<Employee, Customer, CustomerEmployeeComposition>() {
                    @Override
                    public CustomerEmployeeComposition apply(Employee order, Customer customer) {
                        CustomerEmployeeComposition customerEmployeeComposition = new CustomerEmployeeComposition(customer, order);
                        System.out.println("Created result: " + customerEmployeeComposition);
                        return customerEmployeeComposition;
                    }
                });

        employeeStream.foreach((x, Y) -> System.out.println("employeeStream: " + Y.toString()));

        // write the enriched order to the enriched-order topic
//        customerOrdersStream.to("topic-customer", Produced.with(Serdes.String(), CustomSerDes.customerEmployeeCompositionSerde()));

        return new KafkaStreams(builder.build(), streamsConfiguration);
    }
}
