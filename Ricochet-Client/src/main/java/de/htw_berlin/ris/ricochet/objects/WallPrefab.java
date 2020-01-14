package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public abstract class WallPrefab {
    private static Logger log = LogManager.getLogger();

    public static GameObject simpleBorderWall(ObjectId objectId, SGameObject sGameObject, Scene whichScene) {

        return new GameObject(
                objectId,
                sGameObject.getPosition(),
                sGameObject.getWidth(),
                sGameObject.getHeight(),
                BodyType.STATIC,
                1f,
                0.5f,
                whichScene );
    }

    public static GameObject simpleWall(ObjectId objectId, WallPrefabConfig.PrefabType type , Vec2 position, float sizeX, float sizeY, Scene whichScene) {

        switch (type) {

            case CellWall:
                GameObject Wall =  new GameObject(objectId,position, sizeX, sizeY ,BodyType.STATIC, 1f, 0.5f,whichScene );
                return Wall;
        }

        return null;

    }


}
