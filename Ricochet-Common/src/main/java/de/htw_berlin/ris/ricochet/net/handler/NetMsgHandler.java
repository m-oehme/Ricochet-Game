package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface NetMsgHandler<T extends NetMessage, S extends HandlerObserver> {

    Class<T> getType();

    void handle(T message);

    void registerObserver(S handlerObserver);
    void unregisterObserver(S handlerObserver);
}
