package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public interface LoginObserver extends HandlerObserver {
    void onNewClientId(ClientId clientId);
}