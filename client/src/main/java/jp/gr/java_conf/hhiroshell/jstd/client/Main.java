package jp.gr.java_conf.hhiroshell.jstd.client;

import java.net.URI;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            System.exit(0);
        }
        URI uri = null;
        try {
            uri = URI.create(args[0]);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "[JSTD]: ERROR: The given uri string violates RFC2396 syntax...");
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> SocketClient.closeCurrentSession()
        ));

        SocketClient.connectAndWait(uri);
    }

    private static void usage() {
        System.out.println("usage");
    }
}
