package eu.hammarback;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

@Path("/{topicName}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProducerResource {

  private final MessageProducer messageProducer;

  public ProducerResource(MessageProducer messageProducer) {
    this.messageProducer = messageProducer;
  }

  @POST
  public Response send(@PathParam("topicName") String topicName, @NotNull Map<String, String> message) {
    String key = getField(message, "key", "Missing 'key'");
    String value = getField(message, "value", "Missing 'value'");
    messageProducer.send(topicName, key, value);
    return Response.ok().build();
  }

  private String getField(Map<String, String> message, String key, String errorMessage) {
    return Optional.ofNullable(message.get(key)).orElseThrow(() -> new IllegalArgumentException(errorMessage));
  }
  
}
