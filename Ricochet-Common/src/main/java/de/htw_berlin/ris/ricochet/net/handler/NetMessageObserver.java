package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface NetMessageObserver<T extends NetMessage> {

    void onNewMessage(T message);
}
