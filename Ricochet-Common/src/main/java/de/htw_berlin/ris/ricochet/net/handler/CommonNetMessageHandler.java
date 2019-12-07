package de.htw_berlin.ris.ricochet.net.handler;


import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class CommonNetMessageHandler<T extends NetMessage> implements NetMessageHandler<T> {

    private static Logger log = LogManager.getLogger();

    protected HashMap<Class<? extends NetMessageObserver>, NetMessageObserver<T>> messageObserverHashMap = new HashMap<>();

    @Override
    public void handle(T message) {
        messageObserverHashMap.values().forEach(chatMessageObserver -> {
            chatMessageObserver.onNewMessage(message);
        });
    }

    @Override
    public NetMessageHandler<T> registerObserver(NetMessageObserver<T> handlerObserver) {
        messageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
        return this;
    }

    @Override
    public void unregisterObserver(NetMessageObserver<T> handlerObserver) {
        messageObserverHashMap.remove(handlerObserver.getClass());
    }

}
