package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.objects.ObjectId;
import de.htw_berlin.ris.ricochet.objects.shared.SGameObject;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.List;

public class WorldRequestMessage extends WorldMessage implements NetMessage {
    private Vec2 worldSize;

    public WorldRequestMessage(ClientId clientId) {
        super(clientId, MessageScope.SELF);
    }

    public Vec2 getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(Vec2 worldSize) {
        this.worldSize = worldSize;
    }
}
