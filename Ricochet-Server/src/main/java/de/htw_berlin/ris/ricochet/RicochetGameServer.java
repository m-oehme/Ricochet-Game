package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.manager.ServerNetManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RicochetGameServer {
    private static Logger log = LogManager.getLogger();

    private static ServerNetManager serverNetManager;
    private static ClientManager clientManager = new ClientManager();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        serverNetManager = new ServerNetManager(clientManager,8080,8081);
        serverNetManager.startServer();

    }

    static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Shutting Down!");
            serverNetManager.stopServer();
        }
    }
}
