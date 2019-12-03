package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.chat.ServerChatManager;
import de.htw_berlin.ris.ricochet.net.handler.ChatMessageHandler;
import de.htw_berlin.ris.ricochet.net.manager.ServerNetManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RicochetGameServer {
    private static Logger log = LogManager.getLogger();

    private static ServerNetManager serverNetManager;
    private static ClientManager clientManager = new ClientManager();

    private static ServerChatManager serverChatManager;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        serverNetManager = new ServerNetManager(clientManager,8080);
        serverNetManager.startServer();

        try {
            log.info("Server ready at: " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        serverChatManager = new ServerChatManager(clientManager);

        serverNetManager.registerHandlerForAll(ChatMessageHandler.get().registerObserver(serverChatManager));
    }

    static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Shutting Down!");
            serverNetManager.stopServer();
        }
    }
}
