package de.htw_berlin.ris.ricochet.net.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class ClientSocketListener extends SocketListener {
    private static Logger log = LogManager.getLogger();
    private final ClientNetManager clientNetManager;

    public ClientSocketListener(ClientNetManager clientNetManager, int port) {
        super(port);
        this.clientNetManager = clientNetManager;
    }

    @Override
    protected void onMessageReceived(Socket receiveSocket) {
        clientNetManager.getNetManger().processSocket(receiveSocket);
    }

}
