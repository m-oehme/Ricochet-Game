package de.htw_berlin.ris.ricochet.net.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

class ServerSocketListener extends SocketListener {
    private static Logger log = LogManager.getLogger();

    private ServerNetManager serverNetManager;

    public ServerSocketListener(ServerNetManager serverNetManager, int port) {
        super(port);
        this.serverNetManager = serverNetManager;
    }

    @Override
    protected void onMessageReceived(Socket receiveSocket) {
        serverNetManager.getLoginManager().processSocket(receiveSocket);
        serverNetManager.getClientsHolder().values().forEach(netManager -> {
            netManager.processSocket(receiveSocket);
        });
    }
}
