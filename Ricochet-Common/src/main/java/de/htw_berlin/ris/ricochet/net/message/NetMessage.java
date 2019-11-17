package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

import java.io.Serializable;

public interface NetMessage extends Serializable {
    ClientId getClientId();
    void setClientId(ClientId clientId);
}
