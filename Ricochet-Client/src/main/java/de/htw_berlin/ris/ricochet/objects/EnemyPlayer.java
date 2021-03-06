package de.htw_berlin.ris.ricochet.objects;

import de.htw_berlin.ris.ricochet.Entities.GameWorld;
import de.htw_berlin.ris.ricochet.Entities.Scene;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
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

        if (!(this instanceof Player)) {
            ClientNetManager.get().getHandlerFor(ObjectMoveMessage.class).registerObserver(this);
        }
    }

}
