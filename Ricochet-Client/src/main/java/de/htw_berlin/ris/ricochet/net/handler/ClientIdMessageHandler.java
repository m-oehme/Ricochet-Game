package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.ClientIdMessage;

public class ClientIdMessageHandler implements NetMsgHandler<ClientIdMessage> {

    private ClientNetManager clientNetManager;

    public ClientIdMessageHandler(ClientNetManager clientNetManager) {
        this.clientNetManager = clientNetManager;
    }

    @Override
    public Class<ClientIdMessage> getType() {
        return ClientIdMessage.class;
    }

    @Override
    public void handle(ClientIdMessage message) {
        clientNetManager.setClientId(message.getClientId());
    }
}
