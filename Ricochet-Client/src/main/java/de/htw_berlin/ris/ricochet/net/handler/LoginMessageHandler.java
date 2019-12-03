package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

public class LoginMessageHandler extends NetMessageHandlerImpl<LoginMessage, LoginObserver> {
    private static LoginMessageHandler INSTANCE = null;

    public static LoginMessageHandler get() {
        if( INSTANCE == null ) INSTANCE = new LoginMessageHandler();
        return INSTANCE;
    }

    @Override
    public Class<LoginMessage> getType() {
        return LoginMessage.class;
    }

    @Override
    public void handle(LoginMessage message) {
        messageObserverHashMap.values().forEach(loginObserver -> {
            loginObserver.onNewClientId(message.getClientId());
        });
    }
}
