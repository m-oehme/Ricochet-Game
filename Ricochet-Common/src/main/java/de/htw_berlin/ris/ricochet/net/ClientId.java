package de.htw_berlin.ris.ricochet.net;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientId implements Comparable<ClientId>, Serializable {
    private String clientId;

    public ClientId(InetAddress inetAddress) {
        clientId = Integer.toHexString(inetAddress.hashCode());
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public int compareTo(ClientId o) {
        return clientId.compareTo(o.clientId);
    }

}
