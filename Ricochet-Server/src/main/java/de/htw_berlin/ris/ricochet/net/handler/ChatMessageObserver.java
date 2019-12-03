package de.htw_berlin.ris.ricochet.net.handler;

import java.net.InetAddress;

public interface ChatMessageObserver extends HandlerObserver {
    void onNewMessage(String chatMessage);
}
