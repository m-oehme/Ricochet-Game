package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.RicochetServerApplication;
import de.htw_berlin.ris.ricochet.RicochetServerMain;
import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.world.*;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SCompanionAI;
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

public class GameWorldComponent implements Runnable, NetMessageObserver<WorldRequestMessage> {
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
    private GameWorldComponent(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    private ClientManager clientManager;
    private GameWorld gameWorld = new GameWorld();

    @Override
    public void run() {
        initialize();
    }

    private void initialize() {
        gameWorld.generateStaticWorld(RicochetServerApplication.get().getWorldWidth(),RicochetServerApplication.get().getWorldHeight());
        log.info("New World of size "+ gameWorld.getWorldSize() +" generated!");
    }

    @Override
    public void onNewMessage(WorldRequestMessage worldRequestMessage) {
        Map<ObjectId, SPlayer> playerMap = gameWorld.getPlayerObjects().entrySet().stream()
                .filter(map -> !worldRequestMessage.getClientId().equals(map.getValue().getClientId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        worldRequestMessage.setWorldSize(gameWorld.getWorldSize());
        worldRequestMessage.setPlayerList(new HashMap<>(playerMap));

        log.debug("Sending World Size: " + gameWorld.getWorldSize());
        clientManager.sendMessageToClients(worldRequestMessage);
    }

    public final NetMessageObserver<WorldRequestScenesMessage> onRequestWorldScenes = worldRequestScenesMessage -> {
        HashMap<ObjectId, SGameObject> gameObjectHashMap = new HashMap<>();
        worldRequestScenesMessage.getSceneList().forEach(vec2 -> {
            gameObjectHashMap.putAll(gameWorld.getGameObjectsForScene(vec2));
        });

        worldRequestScenesMessage.setGameObjectList(gameObjectHashMap);
        worldRequestScenesMessage.setWorldSize(gameWorld.getWorldSize());


        log.debug("Sending World " + worldRequestScenesMessage.getSceneList().size() + "Scenes");
        clientManager.sendMessageToClients(worldRequestScenesMessage);
    };

    public final NetMessageObserver<ObjectCreateMessage> onCreateObject = objectCreateMessage -> {
        SGameObject sGameObject = objectCreateMessage.getSGameObject();

        ObjectId objectId = gameWorld.addGameObject(sGameObject);
        objectCreateMessage.setObjectId(objectId);

        clientManager.sendMessageToClients(objectCreateMessage);

        log.debug("New Object - Type: " + sGameObject.getClass().getSimpleName() + " ID: " + objectId);
        log.debug("New Object - Position: " + sGameObject.getPosition());
    };

    public final NetMessageObserver<ObjectMoveMessage> onMoveObject = objectMoveMessage -> {
        boolean isMoveOk = gameWorld.updateGameObjectPosition(objectMoveMessage.getObjectId(), objectMoveMessage.getScene(), objectMoveMessage.getPosition());

        if (isMoveOk) {
            clientManager.sendMessageToClients(objectMoveMessage);
        } else {
            SGameObject gameObject = gameWorld.getGameObject(objectMoveMessage.getObjectId());
            ObjectMoveMessage collisionMessage = new ObjectMoveMessage(objectMoveMessage.getClientId(), objectMoveMessage.getObjectId(), gameObject.getScene(), gameObject.getPosition());
            collisionMessage.setMessageScope(MessageScope.EVERYONE);
            clientManager.sendMessageToClients(collisionMessage);
        }
    };

    public final NetMessageObserver<ObjectDestroyMessage> onDestroyObject = objectDestroyMessage -> {
        gameWorld.removeGameObject(objectDestroyMessage.getObjectId());

        clientManager.sendMessageToClients(objectDestroyMessage);
        log.debug(String.format("Object Destroyed: %s", objectDestroyMessage.getObjectId()));
    };

    public void removeAllObjectsForPlayer(ClientId clientId) {
        ObjectId objectId = gameWorld.removePlayerObject(clientId);

        gameWorld.getDynamicGameObjects().entrySet().stream()
                .filter(entry -> entry.getValue() instanceof SCompanionAI)
                .filter(entry -> ((SCompanionAI) entry.getValue()).getGuardianPlayer().equals(objectId))
                .findFirst().ifPresent(entry -> {
                    gameWorld.removeGameObject(entry.getKey());
                    clientManager.sendMessageToClients(new ObjectDestroyMessage(clientId, entry.getKey()));
                });

        if (objectId != null) {
            clientManager.sendMessageToClients(new ObjectDestroyMessage(clientId, objectId));
        }

    }
}
