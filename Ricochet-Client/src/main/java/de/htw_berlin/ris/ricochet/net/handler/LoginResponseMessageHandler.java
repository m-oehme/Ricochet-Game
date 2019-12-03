package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

import java.util.HashMap;

public class LoginResponseMessageHandler implements NetMsgHandler<LoginMessage, LoginResponseObserver> {

    private HashMap<Class<? extends LoginResponseObserver>, LoginResponseObserver> clientIdObserverHolder = new HashMap<>();

    @Override
    public Class<LoginMessage> getType() {
        return LoginMessage.class;
    }

    @Override
    public void handle(LoginMessage message) {
        clientIdObserverHolder.values().forEach(loginResponseObserver -> {
            loginResponseObserver.onNewClientId(message.getClientId());
        });
    }

    public void registerObserver(LoginResponseObserver loginResponseObserver) {
        clientIdObserverHolder.put(loginResponseObserver.getClass(), loginResponseObserver);
    }

    public void unregisterObserver(LoginResponseObserver loginResponseObserver) {
        clientIdObserverHolder.remove(loginResponseObserver.getClass());
    }
}
