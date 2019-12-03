package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

import java.util.HashMap;

public class LoginMessageHandler implements NetMsgHandler<LoginMessage, LoginObserver> {

    private HashMap<Class<? extends LoginObserver>, LoginObserver> clientIdObserverHolder = new HashMap<>();

    @Override
    public Class<LoginMessage> getType() {
        return LoginMessage.class;
    }

    @Override
    public void handle(LoginMessage message) {
        clientIdObserverHolder.values().forEach(loginObserver -> {
            loginObserver.onNewClientId(message.getClientId());
        });
    }

    public void registerObserver(LoginObserver loginObserver) {
        clientIdObserverHolder.put(loginObserver.getClass(), loginObserver);
    }

    public void unregisterObserver(LoginObserver loginObserver) {
        clientIdObserverHolder.remove(loginObserver.getClass());
    }
}
