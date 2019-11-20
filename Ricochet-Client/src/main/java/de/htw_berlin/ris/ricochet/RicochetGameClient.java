package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.ClientIdMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.ClientIdObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.ClientIdMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class RicochetGameClient {
    private static Logger log = LogManager.getLogger();

    private static ClientId clientId = null;
    private static ClientNetManager clientNetManager;

    public static void main(String[] args) {
        try {
            startNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void startNetwork() throws IOException {
        clientNetManager = ClientNetManager.create(InetAddress.getLocalHost(), 8080, 8081);
        clientNetManager.startMessageReceiver();

        clientNetManager.registerHandlerObserver(ClientIdMessage.class, clientIdObserver);
        clientNetManager.sentLogin();
    }

    private static ClientIdObserver clientIdObserver = clientIdValue -> {
        clientId = clientIdValue;

        log.debug("Received ID from Server: " + clientId);
    };
}
