package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SPlayer;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GameWorld {
    private WorldGenerator worldGenerator = new SimpleWorldGenerator();
    private ConcurrentHashMap<ObjectId, SPlayer> playerObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> dynamicGameObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> staticGameObjects = new ConcurrentHashMap<>();

    public ObjectId addGameObject(SGameObject gameObject) {

        ObjectId objectId;
        do {
            objectId = new ObjectId(gameObject);
        } while (dynamicGameObjects.containsKey(objectId) || playerObjects.containsKey(objectId) || staticGameObjects.containsKey(objectId));

        if (gameObject instanceof SPlayer) {
            playerObjects.put(objectId, (SPlayer) gameObject);
        } else {
            dynamicGameObjects.put(objectId, gameObject);
        }
        return objectId;
    }

    public ObjectId removePlayerObject(ClientId clientId) {
        Optional<ObjectId> objectId = playerObjects.entrySet().stream()
                .filter(map -> clientId.equals(map.getValue().getClientId()))
                .map(Map.Entry::getKey)
                .findFirst();

        if (objectId.isPresent()) {
            ObjectId id = objectId.get();

            playerObjects.remove(id);
            return id;
        } else {
            return null;
        }
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

    public void generateStaticWorld(int sizeX, int sizeY) {
        List<SGameObject> sGameObjects = worldGenerator.generateWorld(sizeX, sizeY);

        for (SGameObject sGameObject : sGameObjects) {
            ObjectId objectId;
            do {
                objectId = new ObjectId(sGameObject);
            } while (staticGameObjects.containsKey(objectId));

            staticGameObjects.put(objectId, sGameObject);
        }

    }
}
