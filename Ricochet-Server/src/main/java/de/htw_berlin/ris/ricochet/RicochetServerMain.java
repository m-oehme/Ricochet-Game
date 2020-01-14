package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.ServerNetComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

public class RicochetServerMain {
    private static Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        int worldWidth = Integer.parseInt(args[1]);
        int worldHeight = Integer.parseInt(args[1]);

        RicochetServerApplication.initialize(worldWidth, worldHeight);
    }

    static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Shutting Down!");
            ServerNetComponent.get().stopServer();
        }
    }
}
