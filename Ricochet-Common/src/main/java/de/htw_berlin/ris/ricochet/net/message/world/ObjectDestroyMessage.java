package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;
import de.htw_berlin.ris.ricochet.objects.ObjectId;

public class ObjectDestroyMessage extends WorldMessage implements NetMessage {
    private ObjectId objectId;

    public ObjectDestroyMessage(ClientId clientId, ObjectId objectId) {
        super(clientId, MessageScope.EVERYONE);
        this.objectId = objectId;
    }

    public ObjectId getObjectId() {
        return objectId;
    }
}
