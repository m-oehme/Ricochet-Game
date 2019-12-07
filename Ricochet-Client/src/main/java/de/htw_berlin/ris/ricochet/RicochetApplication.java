package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.ChatMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.ChatMessageObserver;
import de.htw_berlin.ris.ricochet.net.handler.LoginMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.LoginObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

class RicochetApplication {
    private static Logger log = LogManager.getLogger();

    private static RicochetApplication INSTANCE = null;
    public static RicochetApplication get() {
        return INSTANCE;
    }
    public static RicochetApplication initialize(InetAddress serverAddress, int serverPort) {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetApplication(serverAddress, serverPort);
        }
        return INSTANCE;
    }

    private ClientId clientId = null;

    public RicochetApplication(InetAddress serverAddress, int serverPort) {
        onInitialize(serverAddress, serverPort);
    }

    private void onInitialize(InetAddress serverAddress, int serverPort) {
        ClientNetManager.create(serverAddress, serverPort);

        LoginMessageHandler loginMessageHandler = new LoginMessageHandler();
        loginMessageHandler.registerObserver(ClientNetManager.get()).registerObserver(loginObserver);

        ChatMessageHandler chatMessageHandler = new ChatMessageHandler();
        chatMessageHandler.registerObserver(chatMessageObserver);

        ClientNetManager.get().registerHandler(chatMessageHandler);
        ClientNetManager.get().registerHandler(loginMessageHandler);
    }

    private void onStarted() {

    }

    private LoginObserver loginObserver = clientIdValue -> {
        clientId = clientIdValue;

        log.debug("Received ID from Server: " + clientId);
    };

    private ChatMessageObserver chatMessageObserver = chatMessage -> {
        System.out.println("Chat: " + chatMessage.getChatUsername() + " :: " + chatMessage.getChatMessage());
    };

    public ClientId getClientId() {
        return clientId;
    }
}