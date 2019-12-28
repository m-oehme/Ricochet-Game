package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface NetMessageHandler<T extends NetMessage> extends Runnable {

    void handle(T message);

    NetMessageHandler registerObserver(NetMessageObserver<T> handlerObserver);
    void unregisterObserver(NetMessageObserver<T> handlerObserver);

    boolean isRunning();

    void stop();
}
