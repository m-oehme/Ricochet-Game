package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.ClientIdMessage;

import java.util.HashMap;

public class ClientIdMessageHandler implements NetMsgHandler<ClientIdMessage, ClientIdObserver> {

    private HashMap<Class<? extends ClientIdObserver>, ClientIdObserver> clientIdObserverHolder = new HashMap<>();

    @Override
    public Class<ClientIdMessage> getType() {
        return ClientIdMessage.class;
    }

    @Override
    public void handle(ClientIdMessage message) {
        clientIdObserverHolder.values().forEach(clientIdObserver -> {
            clientIdObserver.onNewClientId(message.getClientId());
        });
    }

    public void registerObserver(ClientIdObserver clientIdObserver) {
        clientIdObserverHolder.put(clientIdObserver.getClass(), clientIdObserver);
    }

    public void unregisterObserver(ClientIdObserver clientIdObserver) {
        clientIdObserverHolder.remove(clientIdObserver.getClass());
    }
}
