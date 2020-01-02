package de.htw_berlin.ris.ricochet.net.message.world;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;

public abstract class WorldMessage extends ScopedMessage implements NetMessage {
    public WorldMessage(ClientId clientId, MessageScope messageScope) {
        super(clientId, messageScope);
    }
}
