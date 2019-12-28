package de.htw_berlin.ris.ricochet.world;

import de.htw_berlin.ris.ricochet.objects.SGameObject;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import org.jbox2d.common.Vec2;

import java.util.concurrent.ConcurrentHashMap;

public class GameWorld {
    private ConcurrentHashMap<ObjectId, SGameObject> dynamicGameObjects = new ConcurrentHashMap<>();

    public ObjectId addGameObject(SGameObject SGameObject) {

        ObjectId objectId;
        do {
            objectId = new ObjectId(SGameObject);
        } while (dynamicGameObjects.containsKey(objectId));

        dynamicGameObjects.put(objectId, SGameObject);

        return objectId;
    }

    public void removeGameObject(ObjectId objectId) {
        dynamicGameObjects.remove(objectId);
    }

    public boolean updateGameObjectPosition(ObjectId objectId, Vec2 posistion) {
        if (dynamicGameObjects.containsKey(objectId)) {


            dynamicGameObjects.get(objectId);

            return true;
        }

        return false;
    }
}
