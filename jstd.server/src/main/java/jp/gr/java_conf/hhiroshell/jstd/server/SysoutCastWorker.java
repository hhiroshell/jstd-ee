package jp.gr.java_conf.hhiroshell.jstd.server;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
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

    String addClient(Session clientSession) {
        String id = clientSession.getId();
        clients.put(id, clientSession);
        if (runnerFuture == null || runnerFuture.isDone()) {
            BufferedReader reader = SysoutDelegationManager.setup();
            // no running runner.
            runnerFuture = executor.submit(runner = new Runner(reader));
        }
        return id;
    }

    void removeClient(String id) {
        try {
            Session client = clients.get(id);
            if (client != null || client.isOpen()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clients.remove(id);
        if (clients.isEmpty()) {
            runner.stop();
            while (!runnerFuture.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SysoutDelegationManager.stop();
        }
    }

    void removeAllClient() {
        clients.values().forEach(c -> {
            if (c.isOpen()) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        clients.clear();
        runner.stop();
        while (!runnerFuture.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SysoutDelegationManager.stop();
    }

    private class Runner implements Runnable {

        private boolean running = true;

        private BufferedReader reader;

        private Runner(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            while (isActive()) {
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

        void stop() {
            running = false;
        }

        private boolean isActive() {
            return running;
        }

    }

}
