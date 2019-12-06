package de.htw_berlin.ris.ricochet.net.manager;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientId implements Comparable<ClientId>, Serializable {
    private String clientId;

    public ClientId(InetAddress inetAddress) {
        clientId = Integer.toHexString((int) (inetAddress.hashCode() * Math.random()));
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public int compareTo(ClientId o) {
        return o == null ? -1 : clientId.compareTo(o.clientId);
    }

    @Override
    public String toString() {
        return clientId;
    }
}
