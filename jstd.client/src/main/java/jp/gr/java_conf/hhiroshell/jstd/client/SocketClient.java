package jp.gr.java_conf.hhiroshell.jstd.client;

import javax.websocket.*;
import java.io.IOException;
import java.net.*;

@ClientEndpoint
public class SocketClient {

    private static URI uri;

    private static boolean connected = false;

    private static Session currentSession = null;

    static void connectAndWait(URI u) {
        uri = u;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Class<?> c = SocketClient.class;

        // 接続
        try (Session session = container.connectToServer(c, uri)) {
            currentSession = session;
            connected = true;
            while (connected) {
                System.out.print("Waiting... |\r");
                Thread.sleep(500);
                System.out.print("Waiting... /\r");
                Thread.sleep(500);
                System.out.print("Waiting... -\r");
                Thread.sleep(500);
                System.out.print("Waiting... \\\r");
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    static void closeCurrentSession() {
        if (currentSession == null) {
            return;
        }
        try {
            currentSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        Utils.jstdPrintln("ERROR!");
        Utils.jstdPrintln("Session ID: "+ session.getId());
        Utils.jstdPrintln("Cause: "+ cause.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.print("            \r");
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        connected = false;
        Utils.jstdPrintln("Connection closed.");
    }

    @OnOpen
    public void onOpen(Session session) {
        Utils.jstdPrintln("Successfully connected to the server.");
        Utils.jstdPrintln("Server address: " + uri);
    }

}
