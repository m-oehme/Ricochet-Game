package de.htw_berlin.ris.ricochet.net.handler;


import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public abstract class AbsMessageHandler<T extends NetMessage, S extends HandlerObserver> implements NetMessageHandler<T, S> {
    private static Logger log = LogManager.getLogger();

    protected HashMap<Class<? extends HandlerObserver>, S> messageObserverHashMap = new HashMap<>();

    @Override
    public NetMessageHandler<T, S> registerObserver(S handlerObserver) {
        messageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
        return this;
    }

    @Override
    public void unregisterObserver(S handlerObserver) {
        messageObserverHashMap.remove(handlerObserver.getClass());
    }

}
