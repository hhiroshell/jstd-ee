package jp.gr.java_conf.hhiroshell.jstd.server.sample.jaxrs;

import com.github.ricksbrown.cowsay.Cowsay;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.*;

/**
 * Remote cowsay invoker using JAX-RS
 *
 * @author hhiroshell
 */
@Path("/cowsay")
public class CowsayController {

    private static final String br = System.getProperty("line.separator");

    private static final List<String> cowfiles;
    static {
        List<String> infelicities = Arrays.asList(new String[]{"head-in", "telebears", "sodomized"});
        List<String> c = new LinkedList<>();
        Arrays.stream(Cowsay.say(new String[]{"-l"}).split(br)).forEach(f -> {
            if (!infelicities.contains(f)) {
                c.add(f);
            }
        });
        cowfiles = Collections.unmodifiableList(c);
    }

    /**
     * Print cowsay 'say' message to System.out.
     *
     * @return Echo received message.
     */
    @GET
    @Path("/say")
    public String say(@QueryParam("message") String message) {
        Optional<String> msg = Optional.ofNullable(message);
        String[] said = Cowsay.say(
                new String[]{"-f", getRandomCowfile(), msg.orElse("Moo!")}).split(br);
        Arrays.stream(said).forEach(l -> System.out.println(l));
        return "Said.";
    }

    /**
     * Print cowsay 'think' message to System.out.
     *
     * @return Echo received message.
     */
    @GET
    @Path("/think")
    public String think(@QueryParam("message") String message) {
        Optional<String> msg = Optional.ofNullable(message);
        String[] thought = Cowsay.think(
                new String[]{"-f", getRandomCowfile(), msg.orElse("Moo!")}).split(br);
        Arrays.stream(thought).forEach(l -> System.out.println(l));
        return "Thought.";
    }

    private static String getRandomCowfile() {
        return cowfiles.get(new Random().nextInt(cowfiles.size()));
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
