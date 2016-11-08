package eu.hammarback;

import eu.hammarback.infrastructure.KafkaMessageProducer;
import eu.hammarback.model.MessageProducer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class ProducerApplication extends Application<ProducerConfiguration> {

    @Override
    public void initialize(final Bootstrap<ProducerConfiguration> bootstrap) {
    }

    @Override
    public void run(final ProducerConfiguration config, final Environment environment) {
        Properties configProperties = new Properties();
        configProperties.put(CLIENT_ID_CONFIG, config.clientId);
        configProperties.put(BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers);
        configProperties.put(KEY_SERIALIZER_CLASS_CONFIG, config.keySerializer);
        configProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, config.valueSerializer);

        MessageProducer messageProducer = new KafkaMessageProducer(configProperties);
        environment.lifecycle().manage(messageProducer);
        environment.jersey().register(new ProducerResource(messageProducer));
    }


    public static void main(final String[] args) throws Exception {
        new ProducerApplication().run(args);
    }

}
