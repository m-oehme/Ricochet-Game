package de.htw_berlin.ris.ricochet;


import de.htw_berlin.ris.ricochet.chat.ServerChatComponent;
import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.ServerNetComponent;
import de.htw_berlin.ris.ricochet.world.GameWorldComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RicochetServerApplication {
    private static Logger log = LogManager.getLogger();

    private static RicochetServerApplication INSTANCE = null;
    private final int worldWidth;
    private final int worldHeight;

    public static RicochetServerApplication get() {
        return INSTANCE;
    }
    public static RicochetServerApplication initialize(int worldWidth, int worldHeight) {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetServerApplication(worldWidth, worldHeight);
        }
        return INSTANCE;
    }

    private ExecutorService mainThreadPool = Executors.newCachedThreadPool();
    private ClientManager clientManager = new ClientManager();

    public RicochetServerApplication(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        onInitialize();
        onStarted();
    }

    private void onInitialize() {
        ServerNetComponent.create(clientManager,8080);

        mainThreadPool.execute(ServerChatComponent.create(clientManager));

        ServerNetComponent.get().startServer();
        try {
            log.info("Server ready at: " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void onStarted() {
        mainThreadPool.execute(GameWorldComponent.create(clientManager));
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }
}