package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import org.jbox2d.common.Vec2;

import java.util.HashMap;

public class WorldRequestMessage extends WorldMessage implements NetMessage {
    private Vec2 worldSize;
    private HashMap<ObjectId, SGameObject> gameObjectList;

    public WorldRequestMessage(ClientId clientId) {
        super(clientId, MessageScope.SELF);
    }

    public void setGameObjectList(HashMap<ObjectId, SGameObject> gameObjectList) {
        this.gameObjectList = gameObjectList;
    }

    public HashMap<ObjectId, SGameObject> getGameObjectList() {
        return gameObjectList;
    }

    public Vec2 getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(Vec2 worldSize) {
        this.worldSize = worldSize;
    }
}
