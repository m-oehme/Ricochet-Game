package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.objects.GameObjectData;
import de.htw_berlin.ris.ricochet.objects.ObjectId;

public class ObjectCreateMessage extends WorldMessage implements NetMessage {
    private ObjectId objectId;
    private GameObjectData gameObjectData;

    public ObjectCreateMessage(ClientId clientId, ObjectId objectId, GameObjectData gameObjectData) {
        super(clientId, MessageScope.EVERYONE);
        this.objectId = objectId;
        this.gameObjectData = gameObjectData;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public GameObjectData getGameObjectData() {
        return gameObjectData;
    }
}
