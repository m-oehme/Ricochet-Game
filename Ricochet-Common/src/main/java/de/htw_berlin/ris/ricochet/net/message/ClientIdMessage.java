package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public class ClientIdMessage implements NetMessage {
    protected ClientId clientId = null;

    public ClientIdMessage() {
    }

    @Override
    public ClientId getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(ClientId clientId) {
        this.clientId = clientId;
    }
}
