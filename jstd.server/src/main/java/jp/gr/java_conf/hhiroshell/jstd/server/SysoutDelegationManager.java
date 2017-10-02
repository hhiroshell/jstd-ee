package jp.gr.java_conf.hhiroshell.jstd.server;

import java.io.*;

class SysoutDelegationManager {

    private static PipedWriter pipedWriter = null;

    private static BufferedReader reader = null;

    private static DelegatingPrintStream delegatingPrintStream = null;

    static BufferedReader setup() {
        pipedWriter = new PipedWriter();
        try {
            reader = new BufferedReader(new PipedReader(pipedWriter));
        } catch (IOException e) {
            e.printStackTrace();
        }
        delegatingPrintStream = new DelegatingPrintStream(pipedWriter);
        System.setOut(delegatingPrintStream);
        return reader;
    }

    static void stop() {
        try {
            pipedWriter.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(
                new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)),
                true));
    }

}
