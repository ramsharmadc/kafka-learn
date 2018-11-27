package eu.hammarback.infrastructure;

import eu.hammarback.MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaMessageProducer implements MessageProducer {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final Properties configProperties;

  private Producer<String, String> producer;

  public KafkaMessageProducer(Properties configProperties) {
    this.configProperties = configProperties;
  }

  @Override
  public void start() {
    this.producer = new KafkaProducer<>(configProperties);
  }

  @Override
  public void send(String topicName, String key, String value) {
    ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);
    producer.send(record, (metadata, exception) -> {
      if (exception == null) {
        logger.info("Message with key [{}] successfully sent", key);
      } else {
        logger.warn("Unable to send message with key [{}]: {}", key, exception.getMessage());
      }
    });
  }

  @Override
  public void stop() {
    this.producer.close();
  }

}
