package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.world.*;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.shared.SPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
        initialize();
        while (isRunning) {
            try {
                WorldMessage msg = worldMessageQueue.take();

                if (msg instanceof WorldRequestMessage) {
                    onRequestWorld((WorldRequestMessage) msg);
                } else if (msg instanceof WorldRequestScenesMessage) {
                    onRequestWorldScenes((WorldRequestScenesMessage) msg);
                } else if (msg instanceof ObjectCreateMessage) {
                    onCreateObject((ObjectCreateMessage) msg);
                } else if (msg instanceof ObjectMoveMessage) {
                    onMoveObject((ObjectMoveMessage) msg);
                } else if (msg instanceof ObjectDestroyMessage) {
                    onDestroyObject((ObjectDestroyMessage) msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        gameWorld.generateStaticWorld(4,4);
        log.info("New World of size "+ gameWorld.getWorldSize() +" generated!");
    }

    @Override
    public void onNewMessage(WorldMessage message) {
        worldMessageQueue.offer(message);
    }

    private void onRequestWorld(WorldRequestMessage worldRequestMessage) {
        Map<ObjectId, SPlayer> playerMap = gameWorld.getPlayerObjects().entrySet().stream()
                .filter(map -> !worldRequestMessage.getClientId().equals(map.getValue().getClientId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        worldRequestMessage.setWorldSize(gameWorld.getWorldSize());
        worldRequestMessage.setPlayerList(new HashMap<>(playerMap));

        log.debug("Sending World Size: " + gameWorld.getWorldSize());
        clientManager.sendMessageToClients(worldRequestMessage);
    }

    private void onRequestWorldScenes(WorldRequestScenesMessage worldRequestScenesMessage) {
        HashMap<ObjectId, SGameObject> gameObjectHashMap = new HashMap<>();
        worldRequestScenesMessage.getSceneList().forEach(vec2 -> {
            gameObjectHashMap.putAll(gameWorld.getGameObjectsForScene(vec2));
        });

        worldRequestScenesMessage.setGameObjectList(gameObjectHashMap);
        worldRequestScenesMessage.setWorldSize(gameWorld.getWorldSize());


        log.debug("Sending World " + worldRequestScenesMessage.getSceneList().size() + "Scenes");
        clientManager.sendMessageToClients(worldRequestScenesMessage);
    }

    private void onCreateObject(ObjectCreateMessage objectCreateMessage) {

        SGameObject sGameObject = objectCreateMessage.getSGameObject();

        ObjectId objectId = gameWorld.addGameObject(sGameObject);
        objectCreateMessage.setObjectId(objectId);

        clientManager.sendMessageToClients(objectCreateMessage);

        log.debug("New Object - Type: " + sGameObject.getClass().getSimpleName() + " ID: " + objectId);
        log.debug("New Object - Position: " + sGameObject.getPosition());
    }

    private void onMoveObject(ObjectMoveMessage objectMoveMessage) {
        gameWorld.updateGameObjectPosition(objectMoveMessage.getObjectId(), objectMoveMessage.getScene(), objectMoveMessage.getPosition());

        clientManager.sendMessageToClients(objectMoveMessage);
    }

    private void onDestroyObject(ObjectDestroyMessage objectDestroyMessage) {
        gameWorld.removeGameObject(objectDestroyMessage.getObjectId());

        clientManager.sendMessageToClients(objectDestroyMessage);
        log.debug(String.format("Object Destroyed: %s", objectDestroyMessage.getObjectId()));
    }

    public void removeAllObjectsForPlayer(ClientId clientId) {
        ObjectId objectId = gameWorld.removePlayerObject(clientId);

        if (objectId != null) {
            clientManager.sendMessageToClients(new ObjectDestroyMessage(clientId, objectId));
        }
    }
}
