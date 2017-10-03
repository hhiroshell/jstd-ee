package jp.gr.java_conf.hhiroshell.jstd.server;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * The WebSocket endpoint of standard output casting
 *
 * @author hhiroshell
 */
@ServerEndpoint(value = "/stdcast")
public class SysoutCastEndpoint {

    @Inject
    SysoutCastWorker worker;

    @OnOpen
    public void open(Session session) {
        try {
            worker.addClient(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session session) {
        try {
            worker.removeClient(session.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void error(Throwable t) {
        t.printStackTrace();
        try {
            worker.removeAllClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void print(String message, Session session) {
        System.out.println("message: " + message);
    }

}