package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.ServerNetComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RicochetServerMain {
    private static Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        RicochetServerApplication.initialize();
    }

    static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Shutting Down!");
            ServerNetComponent.get().stopServer();
        }
    }
}
