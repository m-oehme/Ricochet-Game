package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

public class LoginMessageHandler extends NetMessageHandlerImpl<LoginMessage, LoginObserver> {

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
