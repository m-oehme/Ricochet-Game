package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.net.handler.LoginMessageHandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetManager {

    private final int serverPort;
    private final int clientPort;

    private ExecutorService netManagerThreadPool = Executors.newCachedThreadPool();
    private ServerSocketListener serverSocketListener;

    private NetManager loginManager;
    private HashMap<ClientId, NetManager> clientsHolder = new HashMap<>();

    public ServerNetManager(int serverPort, int clientPort) {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void startServer() {
        serverSocketListener = new ServerSocketListener(this, this.serverPort);
        netManagerThreadPool.execute(serverSocketListener);

        loginManager = new NetManager();
        loginManager.register(new LoginMessageHandler(this));

        netManagerThreadPool.execute(loginManager);
    }

    public void addClient(InetAddress clientInetAddress) {
        NetManager clientNetManager = new NetManager(clientInetAddress, clientPort);

        clientsHolder.put(new ClientId(clientInetAddress), clientNetManager);
        netManagerThreadPool.execute(clientNetManager);
    }

    public NetManager getLoginManager() {
        return loginManager;
    }
    public HashMap<ClientId, NetManager> getClientsHolder() {
        return clientsHolder;
    }
}
