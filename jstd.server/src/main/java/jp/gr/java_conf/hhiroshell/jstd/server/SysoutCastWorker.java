package jp.gr.java_conf.hhiroshell.jstd.server;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@ApplicationScoped
class SysoutCastWorker {

    @Resource
    ManagedExecutorService executor;

    private final Map<String, Session> clients = new ConcurrentHashMap<>();

    private Future<?> runnerFuture;

    private Runner runner = null;

    String addClient(Session clientSession) throws IOException {
        String id = clientSession.getId();
        clients.put(id, clientSession);
        if (runnerFuture == null || runnerFuture.isDone()) {
            // no running runner.
            BufferedReader reader = SysoutDelegationManager.setup(true);
            runnerFuture = executor.submit(runner = new Runner(reader));
        }
        return id;
    }

    void removeClient(String id) throws IOException {
        Session client = clients.get(id);
        if (client != null || client.isOpen()) {
            client.close();
        }
        clients.remove(id);
        if (clients.isEmpty()) {
            runner.deactivate();
            while (!runnerFuture.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SysoutDelegationManager.teardown();
        }
    }

    void removeAllClient() throws IOException {
        for (Session client : clients.values()) {
            if (client.isOpen()) {
                client.close();
            }
        }
        clients.clear();
        runner.deactivate();
        while (!runnerFuture.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SysoutDelegationManager.teardown();
    }

    private class Runner implements Runnable {

        private boolean isActive = false;

        private BufferedReader reader;

        private Runner(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            isActive = true;
            while (isActive) {
                String line = null;
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line == null || line.length() == 0) {
                    break;
                }
                try {
                    for (Session session : clients.values()) {
                        session.getBasicRemote().sendText(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void deactivate() {
            isActive = false;
        }

    }

}
