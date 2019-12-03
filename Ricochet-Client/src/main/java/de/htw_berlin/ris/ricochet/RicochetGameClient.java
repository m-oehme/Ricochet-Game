package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.LoginResponseObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class RicochetGameClient {
    private static Logger log = LogManager.getLogger();

    private static ClientId clientId = null;
    private static ClientNetManager clientNetManager;

    public static void main(String[] args) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(args[0]);
        int serverPort = Integer.parseInt(args[1]);

        startNetwork(serverAddress, serverPort);
    }

    private static void startNetwork(InetAddress serverAddress, int serverPort) throws IOException {
        clientNetManager = ClientNetManager.create(serverAddress, serverPort, 8081);
        clientNetManager.startMessageReceiver();

        clientNetManager.registerHandlerObserver(LoginMessage.class, loginResponseObserver);
        clientNetManager.sentLogin();
    }

    private static LoginResponseObserver loginResponseObserver = clientIdValue -> {
        clientId = clientIdValue;

        log.debug("Received ID from Server: " + clientId);
    };
}
