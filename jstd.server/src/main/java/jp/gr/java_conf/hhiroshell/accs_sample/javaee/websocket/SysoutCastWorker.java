package jp.gr.java_conf.hhiroshell.accs_sample.javaee.websocket;

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

    private Runner runner = null;

    private Future<?> runnerFuture;

    String addClient(Session clientSession) {
        String id = clientSession.getId();
        clients.put(id, clientSession);
        if (runnerFuture == null || runnerFuture.isDone()) {
            // no running runner.
            runner = new Runner();
            runnerFuture = executor.submit(runner);
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
        }
    }

    class Runner implements Runnable {

        private boolean running;

        @Override
        public void run() {
            PipedWriter pipedWriter = new PipedWriter();
            try (BufferedReader reader = new BufferedReader(new PipedReader(pipedWriter))) {
                System.out.println("delegating start.");
                System.setOut(new DelegatingPrintStream(pipedWriter));
                // ?
                //System.setErr(new DelegatingPrintStream(pipedWriter));
                running = true;
                while (running) {
                    String line = reader.readLine();
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
                resetSysout();
                System.out.println("delegating end.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void stop() {
            running = false;
        }

        private void resetSysout() {
            System.setOut(new PrintStream(
                    new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)),
                    true));
            System.setErr(new PrintStream(
                    new BufferedOutputStream(new FileOutputStream(FileDescriptor.err)),
                    true));
        }

    }

}
