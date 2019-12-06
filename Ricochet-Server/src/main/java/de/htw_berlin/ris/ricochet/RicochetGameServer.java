package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.chat.ServerChatComponent;
import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.ServerNetComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RicochetGameServer {
    private static Logger log = LogManager.getLogger();

    private static ClientManager clientManager = new ClientManager();
    private static ExecutorService mainThreadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        initialization();
    }

    private static void initialization() {
        ServerNetComponent.create(clientManager,8080);

        ServerChatComponent.create(clientManager);
        mainThreadPool.execute(ServerChatComponent.get());

        ServerNetComponent.get().startServer();
        try {
            log.info("Server ready at: " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Shutting Down!");
            ServerNetComponent.get().stopServer();
        }
    }
}
