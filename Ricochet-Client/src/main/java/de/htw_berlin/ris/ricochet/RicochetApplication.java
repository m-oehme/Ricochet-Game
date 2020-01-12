package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.general.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.world.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;

import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

public class RicochetApplication {
    private static Logger log = LogManager.getLogger();

    private static RicochetApplication INSTANCE = null;
    public static RicochetApplication get() {
        return INSTANCE;
    }
    public static RicochetApplication initialize(InetAddress serverAddress, int serverPort) {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetApplication();
        }
        try {
            INSTANCE.onInitialize(serverAddress, serverPort);
            INSTANCE.onStarted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }
    private RicochetApplication() { }

    private LinkedBlockingQueue<Boolean> result = new LinkedBlockingQueue<>();

    private void onInitialize(InetAddress serverAddress, int serverPort) throws InterruptedException {
        ClientNetManager.create(serverAddress, serverPort);
        log.info("Socket connection initialized");

        setUpMessageHandler();

        ClientNetManager.get().getHandlerFor(WorldRequestMessage.class).registerObserver(worldRequestMessageObserver);

        RicochetGameGUI.get().init();
        log.info("GUI initialized");
    }

    private void onStarted() {

        log.info("Requesting GameWorld from Server");
        ClientNetManager.get().sentMessage(new WorldRequestMessage(ClientNetManager.get().getClientId()));
        log.info("GUI starting");
        RicochetGameGUI.get().Run();

    }

    private NetMessageObserver<WorldRequestMessage> worldRequestMessageObserver = worldRequestMessage -> {
        log.debug("Received World of size: " + worldRequestMessage.getWorldSize());
        RicochetGameGUI.get().generateWorldScenes(worldRequestMessage.getWorldSize(), worldRequestMessage.getPlayerList());
        Vec2 playerPos = RicochetGameGUI.get().setUpPlayerObject();

        RicochetGameGUI.get().loadSceneChunk(playerPos);
    };

    private void setUpMessageHandler() {
        ClientNetManager.get().registerHandler(ChatMessage.class);

        ClientNetManager.get().registerHandler(ObjectCreateMessage.class);
        ClientNetManager.get().registerHandler(ObjectDestroyMessage.class);
        ClientNetManager.get().registerHandler(ObjectMoveMessage.class);

        ClientNetManager.get().registerHandler(WorldRequestMessage.class);
        ClientNetManager.get().registerHandler(WorldRequestScenesMessage.class);

        log.debug("SetUp and Registered Network Message Handlers");
    }
}