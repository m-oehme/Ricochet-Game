package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

import java.net.InetAddress;

public class LoginMessage extends ScopedMessage implements NetMessage {
    private InetAddress inetAddress;

    public LoginMessage() {
        super(null, MessageScope.SELF);
    }

    public LoginMessage(ClientId clientId) {
        super(clientId, MessageScope.SELF);
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
}
