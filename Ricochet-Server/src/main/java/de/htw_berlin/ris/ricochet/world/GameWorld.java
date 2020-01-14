package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SPlayer;
import de.htw_berlin.ris.ricochet.world.collision.Collision;
import de.htw_berlin.ris.ricochet.world.collision.SimpleObjectCollision;
import de.htw_berlin.ris.ricochet.world.generation.MazeWorldGenerator;
import de.htw_berlin.ris.ricochet.world.generation.SimpleWorldGenerator;
import de.htw_berlin.ris.ricochet.world.generation.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameWorld {
    private static Logger log = LogManager.getLogger();

    private WorldGenerator worldGenerator = new MazeWorldGenerator();
    private Collision collision = new SimpleObjectCollision();
    private ConcurrentHashMap<ObjectId, SPlayer> playerObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> dynamicGameObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ObjectId, SGameObject> staticGameObjects = new ConcurrentHashMap<>();

    private Vec2 worldSize;

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

    public boolean updateGameObjectPosition(ObjectId objectId, Vec2 scene, Vec2 position) {
        HashMap<ObjectId, SGameObject> collisionObjects = new HashMap<>(dynamicGameObjects);
        collisionObjects.putAll(staticGameObjects);

        boolean collision = collisionObjects.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(objectId))
                .anyMatch(entry -> this.collision.checkObjectCollision(getGameObject(objectId), position, entry.getValue()));

        if (collision) return false;

        if (playerObjects.containsKey(objectId)) {
            playerObjects.get(objectId).setPosition(position);
            playerObjects.get(objectId).setScene(scene);
            return true;
        } else if (dynamicGameObjects.containsKey(objectId)) {
            dynamicGameObjects.get(objectId).setPosition(position);
            dynamicGameObjects.get(objectId).setScene(scene);
            return true;
        }

        return false;
    }

    public SGameObject getGameObject(ObjectId objectId) {
        if (playerObjects.containsKey(objectId)) {
            return playerObjects.get(objectId);
        } else if (dynamicGameObjects.containsKey(objectId)) {
            return dynamicGameObjects.get(objectId);
        } else {
            return staticGameObjects.get(objectId);
        }
    };

    public ConcurrentHashMap<ObjectId, SPlayer> getPlayerObjects() {
        return playerObjects;
    }
    public ConcurrentHashMap<ObjectId, SGameObject> getDynamicGameObjects() {
        return dynamicGameObjects;
    }
    public ConcurrentHashMap<ObjectId, SGameObject> getStaticGameObjects() {
        return staticGameObjects;
    }

    public HashMap<ObjectId, SGameObject> getGameObjectsForScene(Vec2 scene) {
        Map<ObjectId, SGameObject> staticGameObjects = this.staticGameObjects.entrySet().stream()
                .filter(objectIdSGameObjectEntry -> objectIdSGameObjectEntry.getValue().getScene().equals(scene))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<ObjectId, SGameObject> dynamicGameObjects = this.dynamicGameObjects.entrySet().stream()
                .filter(objectIdSGameObjectEntry -> objectIdSGameObjectEntry.getValue().getScene().equals(scene))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        HashMap<ObjectId, SGameObject> gameObjects = new HashMap<>(staticGameObjects);
        gameObjects.putAll(dynamicGameObjects);

        return gameObjects;
    }

    public void generateStaticWorld(int sizeX, int sizeY) {
        worldSize = new Vec2(sizeX, sizeY);
        List<SGameObject> sGameObjects = worldGenerator.generateWorld(sizeX, sizeY);

        for (SGameObject sGameObject : sGameObjects) {
            ObjectId objectId;
            do {
                objectId = new ObjectId(sGameObject);
            } while (staticGameObjects.containsKey(objectId));

            staticGameObjects.put(objectId, sGameObject);
        }
    }

    public Vec2 getWorldSize() {
        return worldSize;
    }
}
