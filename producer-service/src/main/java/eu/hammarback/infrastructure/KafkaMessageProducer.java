package eu.hammarback.infrastructure;

import eu.hammarback.MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaMessageProducer implements MessageProducer {

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
  public void send(String topicName, String message) {
    ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
    producer.send(record);
  }

  @Override
  public void stop() {
    this.producer.close();
  }

}
