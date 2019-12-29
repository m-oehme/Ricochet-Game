package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.objects.SGameObject;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.SPlayer;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameWorld {
    private ConcurrentHashMap<ObjectId, SPlayer> playerObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> dynamicGameObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> staticGameObjects = new ConcurrentHashMap<>();

    public ObjectId addGameObject(SGameObject gameObject) {

        ObjectId objectId;
        do {
            objectId = new ObjectId(gameObject);
        } while (dynamicGameObjects.containsKey(objectId) || playerObjects.containsKey(objectId));

        if (gameObject instanceof SPlayer) {
            playerObjects.put(objectId, (SPlayer) gameObject);
        } else {
            dynamicGameObjects.put(objectId, gameObject);
        }
        return objectId;
    }

    public ObjectId removePlayerObject(ClientId clientId) {
        ObjectId objectId = playerObjects.entrySet().stream()
                .filter(map -> clientId.equals(map.getValue().getClientId()))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();

        playerObjects.remove(objectId);
        return objectId;
    }

    public void removeGameObject(ObjectId objectId) {
        if (playerObjects.containsKey(objectId)) {
            playerObjects.remove(objectId);
        } else if (dynamicGameObjects.containsKey(objectId)) {
            dynamicGameObjects.remove(objectId);
        }
    }

    public boolean updateGameObjectPosition(ObjectId objectId, Vec2 position) {
        if (playerObjects.containsKey(objectId)) {
            playerObjects.get(objectId).setPosition(position);
            return true;
        } else if (dynamicGameObjects.containsKey(objectId)) {
            dynamicGameObjects.get(objectId).setPosition(position);
            return true;
        }

        return false;
    }

    public ConcurrentHashMap<ObjectId, SPlayer> getPlayerObjects() {
        return playerObjects;
    }

    public HashMap<ObjectId, SGameObject> getDynamicGameObjects() {
        return new HashMap<>(dynamicGameObjects);
    }

    public ConcurrentHashMap<ObjectId, SGameObject> getStaticGameObjects() {
        return staticGameObjects;
    }
}
