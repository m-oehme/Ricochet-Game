package de.htw_berlin.ris.ricochet.net.handler;

import java.net.InetAddress;

public interface LoginMessageObserver extends HandlerObserver {
    void onNewInetAddress(InetAddress inetAddress);
}
