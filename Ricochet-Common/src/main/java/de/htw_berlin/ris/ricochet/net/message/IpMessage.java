package de.htw_berlin.ris.ricochet.net.message;

import java.net.InetAddress;

public class IpMessage extends ClientIdMessage implements NetMessage {
    private InetAddress inetAddress;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

}
