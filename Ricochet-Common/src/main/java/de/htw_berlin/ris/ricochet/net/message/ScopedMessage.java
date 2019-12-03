package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public abstract class ScopedMessage implements NetMessage {
    protected ClientId clientId;
    private MessageScope messageScope;

    public ScopedMessage(ClientId clientId, MessageScope messageScope) {
        this.clientId = clientId;
        this.messageScope = messageScope;
    }

    @Override
    public ClientId getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(ClientId clientId) {
        this.clientId = clientId;
    }

    public MessageScope getMessageScope() {
        return messageScope;
    }

    public void setMessageScope(MessageScope messageScope) {
        this.messageScope = messageScope;
    }
}
