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

class RicochetServerApplication {
    private static Logger log = LogManager.getLogger();

    private static RicochetServerApplication INSTANCE = null;
    public static RicochetServerApplication get() {
        return INSTANCE;
    }
    public static RicochetServerApplication initialize() {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetServerApplication();
        }
        return INSTANCE;
    }

    private ExecutorService mainThreadPool = Executors.newCachedThreadPool();
    private ClientManager clientManager = new ClientManager();

    public RicochetServerApplication() {
        onInitialize();
    }

    private void onInitialize() {
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

    private void onStarted() {

    }

}