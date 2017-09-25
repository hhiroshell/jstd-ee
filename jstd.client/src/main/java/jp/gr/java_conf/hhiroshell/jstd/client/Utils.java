package jp.gr.java_conf.hhiroshell.jstd.client;

class Utils {

    private static final String JSTD_MESSAGE_HEADER = "[JSTD] ";

    static void jstdPrintln(String message) {
        System.out.println(JSTD_MESSAGE_HEADER + message);
    }

}
