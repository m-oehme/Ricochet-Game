package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.ClientManager;
import de.htw_berlin.ris.ricochet.ClientNetUpdate;
import de.htw_berlin.ris.ricochet.net.handler.LoginMessageHandler;
import de.htw_berlin.ris.ricochet.net.handler.LoginMessageObserver;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetManager implements ClientNetUpdate, LoginMessageObserver {

    private ClientManager clientManager;
    private final int serverPort;
    private final int clientPort;

    private ExecutorService netManagerThreadPool = Executors.newCachedThreadPool();
    private ServerSocketListener serverSocketListener;

    private NetManager loginManager;
    private HashMap<ClientId, NetManager> clientsHolder = new HashMap<>();

    public ServerNetManager(ClientManager clientManager, int serverPort, int clientPort) {
        this.clientManager = clientManager;
        this.serverPort = serverPort;
        this.clientPort = clientPort;

        this.clientManager.setClientNetUpdate(this);
    }

    public void startServer() {
        serverSocketListener = new ServerSocketListener(this, this.serverPort);
        netManagerThreadPool.execute(serverSocketListener);

        loginManager = new NetManager();

        LoginMessageHandler loginMessageHandler = new LoginMessageHandler();
        loginMessageHandler.registerObserver(this);
        loginManager.register(loginMessageHandler);

        netManagerThreadPool.execute(loginManager);
    }

    public void addClient(InetAddress clientInetAddress) {
        NetManager clientNetManager = new NetManager(clientInetAddress, clientPort);

        ClientId clientId = new ClientId(clientInetAddress);
        clientsHolder.put(clientId, clientNetManager);
        clientManager.addNewClient(clientId);
        netManagerThreadPool.execute(clientNetManager);
    }

    @Override
    public void onNewMessageForClient(ClientId clientId, NetMessage message) {
        message.setClientId(clientId);
        clientsHolder.get(clientId).send(message);
    }

    @Override
    public void onNewInetAddress(InetAddress inetAddress) {
        addClient(inetAddress);
    }

    public NetManager getLoginManager() {
        return loginManager;
    }
    public HashMap<ClientId, NetManager> getClientsHolder() {
        return clientsHolder;
    }

}
