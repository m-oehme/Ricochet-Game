package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.RicochetGameGUI;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.apache.logging.log4j.util.PropertySource;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import java.util.Comparator;

public class CompanionAI extends EnemyCompanionAI implements Runnable {
    protected Player guardianPlayer;
    private boolean isAlive = true;

    public CompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene, Player guardianPlayer) {
        super(objectId, pos, width, height, whichScene);
        this.guardianPlayer = guardianPlayer;
    }

    @Override
    public void Init() {
        super.Init();

        RicochetGameGUI.get().getMainGUIThreadPool().execute(this);
    }

    @Override
    public void Update() {
        super.Update();
    }

    protected void calculateAndCorrectCollision() {

        // Distance from Guardian Player
        Vec2 distance = guardianPlayer.position.sub(position);
        Vec2 weightGuardian = body.getLinearVelocity();

        if (distance.lengthSquared() > 27 ) {
            weightGuardian = distance;
        } else if (distance.lengthSquared() < 9.0 ) {
            weightGuardian = distance.negate().mul(0.5f);
        } else if (distance.lengthSquared() < 12.0 || distance.lengthSquared() > 25 ) {
            weightGuardian = new Vec2(0,0);
        }

        // Distance from other Objects
        Vec2 weightCollision = new Vec2(0,0);
//        Vec2 distanceCollision = GameWorld.Instance.getCurrentScene().getSceneObjectsDynamic().stream()
//                .min(Comparator.comparing(gameObject -> gameObject.position.sub(position).length()))
//                .get().position.sub(position);
//
//        if (distanceCollision.length() < 1.0) {
//            weightCollision = new Vec2((float) (1/Math.sqrt((double)(distanceCollision.x))),(float) (1/Math.sqrt((double)(distanceCollision.y))));
//
//        } else {
//            weightCollision = new Vec2(1/distanceCollision.x, 1/distanceCollision.y);
//        }


        body.setLinearVelocity(weightGuardian.sub(weightCollision));

        ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), this.position));
    }

    @Override
    public void run() {
        while (isAlive) {
            calculateAndCorrectCollision();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void Destroy() {
        isAlive = false;
        super.Destroy();
    }
}
