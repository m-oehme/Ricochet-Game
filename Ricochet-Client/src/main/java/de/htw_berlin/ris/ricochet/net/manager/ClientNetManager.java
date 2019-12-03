package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.net.handler.*;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetManager implements LoginResponseObserver {
    private static Logger log = LogManager.getLogger();
    private static ClientNetManager INSTANCE = null;

    private final int clientPort;
    private ClientId clientId;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);
    private NetManager netManger;
    private ClientSocketListener clientSocketListener;

    private ClientNetManager(InetAddress serverAddress, int serverPort, int clientPort) {
        this.clientPort = clientPort;

        netManger = new NetManager(serverAddress, serverPort);

        LoginResponseMessageHandler loginResponseMessageHandler = new LoginResponseMessageHandler();
        loginResponseMessageHandler.registerObserver(this);

        netManger.register(loginResponseMessageHandler);
        netManagerThreadPool.execute(netManger);
    }

    public static ClientNetManager create(InetAddress serverAddress, int serverPort, int clientPort) {
        if( INSTANCE == null ) {
            INSTANCE = new ClientNetManager(serverAddress, serverPort, clientPort);
        }
        return INSTANCE;
    }

    public static ClientNetManager get() {
        return INSTANCE;
    }

    public void sentLogin() {
        LoginMessage login = new LoginMessage();

        netManger.send(login);
    }

    public void startMessageReceiver() {
        clientSocketListener = new ClientSocketListener(this, this.clientPort);
        netManagerThreadPool.execute(clientSocketListener);
    }

    public void sentMessage(NetMessage message) {
        message.setClientId(this.clientId);
        netManger.send(message);
    }

    public NetMsgHandler registerHandler(NetMsgHandler<? extends NetMessage, ? extends HandlerObserver> netMsgHandler) {
        return netManger.register(netMsgHandler);
    }

    public <T extends NetMessage> void registerHandlerObserver(Class<T> netMessageClass, HandlerObserver handlerObserver) {
        netManger.getRegisteredHandler(netMessageClass).registerObserver(handlerObserver);
    }

    public NetManager getNetManger() {
        return netManger;
    }

    public ClientId getClientId() {
        return clientId;
    }

    @Override
    public void onNewClientId(ClientId clientId) {
        this.clientId = clientId;
    }
}
