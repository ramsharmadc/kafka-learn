package eu.hammarback.model;

import io.dropwizard.lifecycle.Managed;

public interface MessageProducer extends Managed {

    void send(String topicName, String message);

}
