package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.handler.ChatMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.ChatMessageObserver;
import de.htw_berlin.ris.ricochet.net.handler.LoginMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.LoginObserver;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class RicochetGameClient {
    private static Logger log = LogManager.getLogger();

    private static ClientId clientId = null;

    public static void main(String[] args) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(args[0]);
        int serverPort = Integer.parseInt(args[1]);

        initialization(serverAddress, serverPort);

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nUsername: ");
        String username = scanner.next();

        while (true) {
            System.out.print("\nChat: ");
            String message = scanner.next();

            ClientNetManager.get().sentMessage(new ChatMessage(clientId, MessageScope.EXCEPT_SELF, username, message));
        }
    }

    private static void initialization(InetAddress serverAddress, int serverPort) {
        ClientNetManager.create(serverAddress, serverPort);

        LoginMessageHandler loginMessageHandler = new LoginMessageHandler();
        loginMessageHandler.registerObserver(ClientNetManager.get()).registerObserver(loginObserver);

        ChatMessageHandler chatMessageHandler = new ChatMessageHandler();
        chatMessageHandler.registerObserver(chatMessageObserver);

        ClientNetManager.get().registerHandler(chatMessageHandler);
        ClientNetManager.get().registerHandler(loginMessageHandler);
    }

    private static LoginObserver loginObserver = clientIdValue -> {
        clientId = clientIdValue;

        log.debug("Received ID from Server: " + clientId);
    };

    private static ChatMessageObserver chatMessageObserver = chatMessage -> {
        System.out.println("Chat: " + chatMessage.getChatUsername() + " :: " + chatMessage.getChatMessage());
    };
}
