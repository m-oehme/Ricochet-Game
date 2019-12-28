package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.net.handler.*;
import de.htw_berlin.ris.ricochet.net.message.general.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetManager {
    private static Logger log = LogManager.getLogger();
    private static ClientNetManager INSTANCE = null;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);
    private NetManager netManger;

    private ClientId clientId = null;

    private ClientNetManager(InetAddress serverAddress, int serverPort) {
        netManger = new NetManager(serverAddress, serverPort);

        CommonNetMessageHandler<LoginMessage> loginMessageCommonNetMessageHandler = new CommonNetMessageHandler<LoginMessage>();
        loginMessageCommonNetMessageHandler.registerObserver(loginObserver);
        netManger.register(LoginMessage.class, loginMessageCommonNetMessageHandler);

        netManagerThreadPool.execute(netManger);
    }

    public static ClientNetManager create(InetAddress serverAddress, int serverPort) {
        if( INSTANCE == null ) {
            INSTANCE = new ClientNetManager(serverAddress, serverPort);
        }
        return INSTANCE;
    }

    public static ClientNetManager get() {
        return INSTANCE;
    }

    public void sentMessage(NetMessage message) {
        netManger.send(message);
    }

    public <T extends NetMessage> void registerHandler(Class<T> messageType, NetMessageHandler<T> netMessageHandler) {
        netManger.register(messageType, netMessageHandler);
    }

    public <T extends NetMessage> void registerHandler(Class<T> messageType) {
        netManger.register(messageType, new CommonNetMessageHandler<T>());
    }

    public <T extends NetMessage> NetMessageHandler<T> getHandlerFor(Class<T> messageType) {
        return netManger.getRegisteredHandler(messageType);
    }

    public ClientId getClientId() {
        return clientId;
    }

    private NetMessageObserver<LoginMessage> loginObserver = loginMessage -> {
        clientId = loginMessage.getClientId();

        log.info("Login on server successful");
        log.debug("Received ID from Server: " + clientId);
    };
}
