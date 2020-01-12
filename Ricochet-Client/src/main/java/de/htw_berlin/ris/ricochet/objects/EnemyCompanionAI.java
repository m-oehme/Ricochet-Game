package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class EnemyCompanionAI extends GameObject {

    public EnemyCompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene) {
        super(objectId, pos, width, height, BodyType.DYNAMIC, whichScene);
    }

    @Override
    public void Init() {
        super.Init();

        body.setFixedRotation(true);
    }

    @Override
    protected void renderAtLocalPosition(Vec2 position) {
        ((CompanionAI) this).debugRay();
//        glTranslatef(position.x, position.y, 0);
//        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);

        renderSphere(position,   17f);
    }
}
