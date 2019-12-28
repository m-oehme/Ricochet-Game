package de.htw_berlin.ris.ricochet.net.manager;

public interface NetworkEvent {
    void onNetworkEvent(NetManager netManager, ClientId clientId, String event);
}
