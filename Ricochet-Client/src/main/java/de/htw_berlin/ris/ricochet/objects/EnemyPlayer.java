package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Scene;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class EnemyPlayer extends GameObject {
    public EnemyPlayer(ObjectId objectId, Vec2 pos, float width, float height, BodyType bodyType, Scene whichScene) {
        super(objectId, pos, width, height, bodyType, whichScene);
    }

    @Override
    public void Init() {
        super.Init();

        body.setFixedRotation(true);
    }
}
