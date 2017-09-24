package jp.gr.java_conf.hhiroshell.jstd.server.sample.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Quite Simple, sample implementation for JAX-RS
 *
 * @author hhayakaw
 */
@Path("/rest")
public class RestController {

    /**
     * Print received message to System.out.
     *
     * @return Echo received message.
     */
    @GET
    @Path("/print")
    public String start(@QueryParam("message") String message) {
        System.out.println(message);
        return message;
    }

    /**
     * Send back a fixed String.
     *
     * @return Send back a fixed String.
     */
    @GET
    @Path("/ping")
    public String ping() {
        System.out.println("I'm working...");
        return "I'm working...";
    }

}
