package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


public class LoginMessageHandler implements NetMsgHandler<LoginMessage, LoginMessageObserver> {
    private static Logger log = LogManager.getLogger();

    private HashMap<Class<? extends LoginMessageObserver>, LoginMessageObserver> loginMessageObserverHashMap = new HashMap<>();

    @Override
    public Class<LoginMessage> getType() {
        return LoginMessage.class;
    }

    @Override
    public synchronized void handle(LoginMessage message) {
        log.info("New Client from: " + message.getInetAddress().toString());
        loginMessageObserverHashMap.values().forEach(loginMessageObserver -> {
            loginMessageObserver.onNewInetAddress(message.getInetAddress());
        });
    }

    @Override
    public void registerObserver(LoginMessageObserver handlerObserver) {
        loginMessageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
    }

    @Override
    public void unregisterObserver(LoginMessageObserver handlerObserver) {
        loginMessageObserverHashMap.remove(handlerObserver.getClass());
    }
}
