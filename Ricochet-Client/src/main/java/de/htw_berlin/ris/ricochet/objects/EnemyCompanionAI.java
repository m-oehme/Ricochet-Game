package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class EnemyCompanionAI extends GameObject {

    public EnemyCompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene) {
        super(objectId, pos, width, height, BodyType.KINEMATIC, whichScene);
    }

    @Override
    public void Init() {
        super.Init();

        if (myScene.equals(GameWorld.Instance.getCurrentScene())) {
            body.setFixedRotation(true);
        }
    }
}
