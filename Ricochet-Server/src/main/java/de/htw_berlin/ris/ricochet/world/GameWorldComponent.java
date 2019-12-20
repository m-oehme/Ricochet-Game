package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.message.world.*;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class GameWorldComponent implements Runnable, NetMessageObserver<WorldMessage> {
    private static Logger log = LogManager.getLogger();

    private static GameWorldComponent INSTANCE = null;
    public static GameWorldComponent get() {
        return INSTANCE;
    }
    public static GameWorldComponent create(ClientManager clientManager) {
        if( INSTANCE == null ) {
            INSTANCE = new GameWorldComponent(clientManager);
        }
        return INSTANCE;
    }
    private GameWorldComponent(ClientManager clientManager) { this.clientManager = clientManager; }

    private ClientManager clientManager;

    private boolean isRunning = true;
    private LinkedBlockingQueue<WorldMessage> worldMessageQueue = new LinkedBlockingQueue<>();

    private GameWorld gameWorld = new GameWorld();

    @Override
    public void run() {
        while (isRunning) {
            try {
                WorldMessage msg = worldMessageQueue.take();

                if (msg instanceof ObjectCreateMessage) {
                    onNewCreatedMessage((ObjectCreateMessage) msg);
                } else if (msg instanceof ObjectMoveMessage) {
                    onNewMoveMessage((ObjectMoveMessage) msg);
                } else if (msg instanceof ObjectDestroyMessage) {
                    onNewDestroyMessage((ObjectDestroyMessage) msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNewMessage(WorldMessage message) {
        worldMessageQueue.offer(message);
    }

    private void onNewCreatedMessage(ObjectCreateMessage objectCreateMessage) {

        ObjectId objectId = gameWorld.addGameObject(objectCreateMessage.getGameObjectData());
        objectCreateMessage.setObjectId(objectId);

        clientManager.sendMessageToClients(objectCreateMessage);

        log.debug(String.format("New Object: %s", objectId));
    }

    private void onNewMoveMessage(ObjectMoveMessage objectMoveMessage) {
        gameWorld.updateGameObjectPosition(objectMoveMessage.getObjectId(), objectMoveMessage.getPosition());

        clientManager.sendMessageToClients(objectMoveMessage);
        log.debug(String.format("Object Moved: %s", objectMoveMessage.getObjectId()));
    }

    private void onNewDestroyMessage(ObjectDestroyMessage objectDestroyMessage) {
        gameWorld.removeGameObject(objectDestroyMessage.getObjectId());

        clientManager.sendMessageToClients(objectDestroyMessage);
        log.debug(String.format("Object Destroyed: %s", objectDestroyMessage.getObjectId()));
    }
}
