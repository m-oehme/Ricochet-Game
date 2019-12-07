package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.*;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
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

        CommonNetMessageHandler<LoginMessage> loginMessageHandler = new CommonNetMessageHandler<>();
        loginMessageHandler.registerObserver(ClientNetManager.get()).registerObserver(loginObserver);

        CommonNetMessageHandler<ChatMessage> chatMessageHandler = new CommonNetMessageHandler<>();
        chatMessageHandler.registerObserver(chatMessageObserver);

        ClientNetManager.get().registerHandler(ChatMessage.class, chatMessageHandler);
        ClientNetManager.get().registerHandler(LoginMessage.class, loginMessageHandler);
    }

    private void onStarted() {

    }

    private NetMessageObserver<LoginMessage> loginObserver = loginMessage -> {
        clientId = loginMessage.getClientId();

        log.debug("Received ID from Server: " + clientId);
    };

    private NetMessageObserver<ChatMessage> chatMessageObserver = chatMessage -> {
        System.out.println("Chat: " + chatMessage.getChatUsername() + " :: " + chatMessage.getChatMessage());
    };

    public ClientId getClientId() {
        return clientId;
    }
}