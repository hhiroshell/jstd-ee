package jp.gr.java_conf.hhiroshell.jstd.server;

import java.io.*;

class SysoutDelegationManager {

    private static PipedWriter pipedWriter = null;

    private static BufferedReader reader = null;

    private static DelegatingPrintStream delegatingStream = null;

    private static PrintStream stdout = null;

    static BufferedReader setup(boolean reset) throws IOException {
        if (System.out instanceof DelegatingPrintStream && !reset) {
            return reader;
        }
        closeAllResources();
        pipedWriter = new PipedWriter();
        reader = new BufferedReader(new PipedReader(pipedWriter));
        delegatingStream = new DelegatingPrintStream(pipedWriter);
        System.setOut(delegatingStream);
        return reader;
    }

    static void teardown() throws IOException {
        closeAllResources();
        stdout = new PrintStream(
                new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true);
        System.setOut(stdout);
    }

    private static void closeAllResources() throws IOException {
        if (pipedWriter != null) {
            pipedWriter.close();
        }
        if (reader != null) {
            reader.close();
        }
        if (delegatingStream != null) {
            delegatingStream.close();
        }
        if (stdout != null) {
            stdout.close();
        }
    }

}
