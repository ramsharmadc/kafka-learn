package eu.hammarback;


import eu.hammarback.model.MessageProducer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/{topicName}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProducerResource {

    private final MessageProducer messageProducer;

    public ProducerResource(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @POST
    public Response send(@PathParam("topicName") String topicName, String message) {
        messageProducer.send(topicName, message);
        return Response.ok().build();
    }


}
