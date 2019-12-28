package de.htw_berlin.ris.ricochet.net.manager;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClientId) {
            return this.clientId.equals(((ClientId) o).getClientId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
