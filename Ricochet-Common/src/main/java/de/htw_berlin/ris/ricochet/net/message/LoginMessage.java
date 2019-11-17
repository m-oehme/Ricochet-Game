package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.ClientId;

public class LoginMessage extends IpMessage {
    private ClientId clientId;

    public LoginMessage(ClientId clientId) {
        this.clientId = clientId;
    }
}
