package de.htw_berlin.ris.ricochet.client;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface ClientNetUpdate {
    void onNewMessageForClient(ClientId receiverClientId, NetMessage message);
}
