package eu.hammarback;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class ProducerConfiguration extends Configuration {

    @NotEmpty
    public String bootstrapServers;

    @NotEmpty
    public String clientId;

    @NotEmpty
    public String keySerializer = "org.apache.kafka.common.serialization.ByteArraySerializer";

    @NotEmpty
    public String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
    
}
