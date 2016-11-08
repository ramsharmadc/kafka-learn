package eu.hammarback;

import io.dropwizard.lifecycle.Managed;

public interface MessageConsumer extends Managed {

    void addListener(MessageListener listener);

    interface MessageListener {

        void onMessage(String message);

    }

}
