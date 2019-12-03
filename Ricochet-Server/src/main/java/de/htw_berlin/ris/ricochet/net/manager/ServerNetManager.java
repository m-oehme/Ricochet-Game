package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.ClientManager;
import de.htw_berlin.ris.ricochet.ClientNetUpdate;
import de.htw_berlin.ris.ricochet.net.handler.HandlerObserver;
import de.htw_berlin.ris.ricochet.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetManager implements ClientNetUpdate {

    private ClientManager clientManager;
    private final int serverPort;

    private ExecutorService netManagerThreadPool = Executors.newCachedThreadPool();
    private ServerSocketListener serverSocketListener;

    private HashMap<ClientId, NetManager> clientsHolder = new HashMap<>();

    public ServerNetManager(ClientManager clientManager, int serverPort) {
        this.clientManager = clientManager;
        this.serverPort = serverPort;

        this.clientManager.setClientNetUpdate(this);
    }

    public void startServer() {
        serverSocketListener = new ServerSocketListener(this, this.serverPort);
        netManagerThreadPool.execute(serverSocketListener);
    }

    public void stopServer() {
        serverSocketListener.stop();
        clientsHolder.values().forEach(NetManager::stopServer);

        netManagerThreadPool.shutdown();
    }

    void addClientSocket(Socket receiverSocket) {
        ClientId client = clientManager.addNewClient(receiverSocket.getInetAddress());

        NetManager netManager = new NetManager(receiverSocket, client);

        clientsHolder.put(client, netManager);
        netManagerThreadPool.execute(netManager);

        netManager.send(new LoginMessage(client));
    }

    @Override
    public void onNewMessageForClient(ClientId receiverClientId, NetMessage message) {
        clientsHolder.get(receiverClientId).send(message);
    }

    public void registerHandlerForAll(NetMsgHandler<? extends NetMessage, ? extends HandlerObserver> netMsgHandler) {
        clientsHolder.values().forEach(netManager -> {
            netManager.register(netMsgHandler);
        });
    }

}
