package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.*;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.general.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.general.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectCreateMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectDestroyMessage;
import de.htw_berlin.ris.ricochet.net.message.world.ObjectMoveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

public class RicochetApplication {
    private static Logger log = LogManager.getLogger();

    private static RicochetApplication INSTANCE = null;
    public static RicochetApplication get() {
        return INSTANCE;
    }
    public static RicochetApplication initialize(InetAddress serverAddress, int serverPort) {
        if( INSTANCE == null ) {
            INSTANCE = new RicochetApplication();
        }
        INSTANCE.onInitialize(serverAddress, serverPort);
        INSTANCE.onStarted();
        return INSTANCE;
    }

    private ClientId clientId = null;

    private RicochetApplication() { }

    private void onInitialize(InetAddress serverAddress, int serverPort) {
        ClientNetManager.create(serverAddress, serverPort);
        log.info("Socket connection initialized");

        setUpMessageHandler();
        ClientNetManager.get().getHandlerFor(LoginMessage.class).registerObserver(loginObserver);

        RicochetGameGUI.get().init();
        log.info("GUI initialized");
    }

    private void onStarted() {
        ClientNetManager.get().getHandlerFor(ChatMessage.class).registerObserver(chatMessageObserver);

        log.info("GUI starting");
        RicochetGameGUI.get().Run();
    }

    private NetMessageObserver<LoginMessage> loginObserver = loginMessage -> {
        clientId = loginMessage.getClientId();

        log.info("Login on server successful");
        log.debug("Received ID from Server: " + clientId);
    };

    private NetMessageObserver<ChatMessage> chatMessageObserver = chatMessage -> {
        System.out.println("Chat: " + chatMessage.getChatUsername() + " :: " + chatMessage.getChatMessage());
    };

    public ClientId getClientId() {
        return clientId;
    }

    private void setUpMessageHandler() {
        ClientNetManager.get().registerHandler(LoginMessage.class);
        ClientNetManager.get().registerHandler(ChatMessage.class);

        ClientNetManager.get().registerHandler(ObjectCreateMessage.class);
        ClientNetManager.get().registerHandler(ObjectDestroyMessage.class);
        ClientNetManager.get().registerHandler(ObjectMoveMessage.class);

        log.debug("SetUp and Registered Network Message Handlers");
    }
}