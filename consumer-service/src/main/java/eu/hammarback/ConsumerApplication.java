package eu.hammarback;

import eu.hammarback.infrastructure.KafkaMessageConsumer;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class ConsumerApplication extends Application<ConsumerConfiguration> {

  @Override
  public void run(final ConsumerConfiguration config, final Environment environment) {
    Properties configProperties = new Properties();
    configProperties.put(CLIENT_ID_CONFIG, config.clientId);
    configProperties.put(GROUP_ID_CONFIG, config.groupId);
    configProperties.put(BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers);
    configProperties.put(KEY_DESERIALIZER_CLASS_CONFIG, config.keyDeserializer);
    configProperties.put(VALUE_DESERIALIZER_CLASS_CONFIG, config.valueDeserializer);

    MessageConsumer messageConsumer = new KafkaMessageConsumer(config.topicName, configProperties);
    messageConsumer.addListener(message -> System.out.println("message received = " + message));
    environment.lifecycle().manage(messageConsumer);
  }

  public static void main(final String[] args) throws Exception {
    new ConsumerApplication().run(args);
  }

}
