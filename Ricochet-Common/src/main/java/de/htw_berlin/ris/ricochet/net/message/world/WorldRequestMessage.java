package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.SGameObject;

import java.util.HashMap;

public class WorldRequestMessage extends WorldMessage implements NetMessage {
    private HashMap<ObjectId, SGameObject> gameObjectList;

    public WorldRequestMessage(ClientId clientId, HashMap<ObjectId, SGameObject> gameObjectList) {
        super(clientId, MessageScope.SELF);
        this.gameObjectList = gameObjectList;
    }

    public void setGameObjectList(HashMap<ObjectId, SGameObject> gameObjectList) {
        this.gameObjectList = gameObjectList;
    }

    public HashMap<ObjectId, SGameObject> getGameObjectList() {
        return gameObjectList;
    }
}
