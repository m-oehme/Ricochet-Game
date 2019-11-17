package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public class LoginMessage extends IpMessage {

    public LoginMessage() {
    }

    public LoginMessage(ClientId clientId) {
        this.clientId = clientId;
    }
}
