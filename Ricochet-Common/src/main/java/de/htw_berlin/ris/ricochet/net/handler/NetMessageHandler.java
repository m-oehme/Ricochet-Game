package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface NetMessageHandler<T extends NetMessage, S extends HandlerObserver> {

    Class<T> getType();

    void handle(T message);

    NetMessageHandler registerObserver(S handlerObserver);
    void unregisterObserver(S handlerObserver);
}
