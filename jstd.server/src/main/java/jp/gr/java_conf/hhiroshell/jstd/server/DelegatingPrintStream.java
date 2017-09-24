package jp.gr.java_conf.hhiroshell.jstd.server;

import java.io.*;

public class DelegatingPrintStream extends PrintStream {

    private final PipedWriter writer;

    public DelegatingPrintStream(PipedWriter writer) {
        super(System.out);
        this.writer = writer;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(buf, off, len);
        try {
            writer.write(stream.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) {
        super.write(b);
        try {
            writer.write(b);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
