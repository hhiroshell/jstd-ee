package jp.gr.java_conf.hhiroshell.jstd.server;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Most simple implementation for WebSocket and CDI.
 *
 * @author hhayakaw
 */
@ServerEndpoint(value = "/websocket")
public class SysoutCastEndpoint {

    @Inject
    SysoutCastWorker worker;

    @OnOpen
    public void open(Session session) {
        worker.addClient(session);
    }

    @OnClose
    public void close(Session session) {
        worker.removeClient(session.getId());
    }

    @OnError
    public void error(Throwable t) {
        t.printStackTrace();
    }

    @OnMessage
    public void print(String message, Session session) {
        System.out.println("message: " + message);
    }

}