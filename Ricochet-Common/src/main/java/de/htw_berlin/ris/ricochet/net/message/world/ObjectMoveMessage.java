package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import org.jbox2d.common.Vec2;

public class ObjectMoveMessage extends WorldMessage implements NetMessage {
    private ObjectId objectId;
    private Vec2 position;

    public ObjectMoveMessage(ClientId clientId, ObjectId objectId, Vec2 position) {
        super(clientId, MessageScope.EXCEPT_SELF);

        this.objectId = objectId;
        this.position = position;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Vec2 getPosition() {
        return position;
    }
}
