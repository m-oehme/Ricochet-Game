package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class CompanionAI extends EnemyCompanionAI {
    protected Player guardianPlayer;

    public CompanionAI(ObjectId objectId, Vec2 pos, float width, float height, Scene whichScene, Player guardianPlayer) {
        super(objectId, pos, width, height, whichScene);
        this.guardianPlayer = guardianPlayer;
    }

    @Override
    public void Update() {
        super.Update();

        Vec2 distance = guardianPlayer.position.sub(position);
        if (distance.length() > 5.5 ) {
            body.setLinearVelocity(distance);
        } else if (distance.length() < 3.0 ) {
            body.setLinearVelocity(distance.negate().mul(0.5f));
        } else if (distance.length() < 4.0 || distance.length() > 5.2 ){
            body.setLinearVelocity(new Vec2(0,0));
        }

        ClientNetManager.get().sentMessage(new ObjectMoveMessage(ClientNetManager.get().getClientId(), this.getObjectId(), myScene.getLocation(), this.position));
    }
}
