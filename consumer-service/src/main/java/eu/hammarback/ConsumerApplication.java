package eu.hammarback;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import eu.hammarback.infrastructure.KafkaMessageConsumer;

import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

public class ConsumerApplication extends Application<ConsumerConfiguration> implements MessageConsumer.MessageListener {

    @Override
    public void initialize(final Bootstrap<ConsumerConfiguration> bootstrap) {
    }

    @Override
    public void run(final ConsumerConfiguration config, final Environment environment) {
        Properties configProperties = new Properties();
        configProperties.put(CLIENT_ID_CONFIG, config.clientId);
        configProperties.put(GROUP_ID_CONFIG, config.groupId);
        configProperties.put(BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers);
        configProperties.put(KEY_DESERIALIZER_CLASS_CONFIG, config.keyDeserializer);
        configProperties.put(VALUE_DESERIALIZER_CLASS_CONFIG, config.valueDeserializer);

        MessageConsumer messageConsumer = new KafkaMessageConsumer(config.topicName, configProperties);
        messageConsumer.addListener(this);
        environment.lifecycle().manage(messageConsumer);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("message received = " + message);
    }

    public static void main(final String[] args) throws Exception {
        new ConsumerApplication().run(args);
    }

}
