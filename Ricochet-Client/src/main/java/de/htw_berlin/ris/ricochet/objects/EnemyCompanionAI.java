package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class EnemyCompanionAI extends GameObject {

    public EnemyCompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene) {
        super(objectId, pos, width, height, BodyType.DYNAMIC, whichScene);
    }

    @Override
    public void Init() {
        super.Init();

        body.setFixedRotation(true);

        if (!(this instanceof CompanionAI)) {
            ClientNetManager.get().getHandlerFor(ObjectMoveMessage.class).registerObserver(this);
        }
    }

    @Override
    protected void renderAtLocalPosition(Vec2 position) {
        //this.debugRay();
//        glTranslatef(position.x, position.y, 0);
//        glRotated(Math.toDegrees(body.getAngle()), 0, 0, 1);

        renderSphere(position,   17f);
    }


}
