package de.htw_berlin.ris.ricochet.net.message.general;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;

import java.net.InetAddress;

public class LoginMessage extends ScopedMessage implements NetMessage {
    private InetAddress inetAddress;

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
