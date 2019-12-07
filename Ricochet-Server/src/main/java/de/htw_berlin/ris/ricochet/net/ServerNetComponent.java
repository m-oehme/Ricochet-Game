package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.client.ClientNetUpdate;
import de.htw_berlin.ris.ricochet.chat.ServerChatComponent;
import de.htw_berlin.ris.ricochet.net.handler.CommonNetMessageHandler;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.NetManager;
import de.htw_berlin.ris.ricochet.net.manager.ServerSocketListener;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetComponent implements ClientNetUpdate {

    private static ServerNetComponent INSTANCE = null;
    public static ServerNetComponent get() {
        return INSTANCE;
    }
    public static ServerNetComponent create(ClientManager clientManager, int serverPort) {
        if( INSTANCE == null ) {
            INSTANCE = new ServerNetComponent(clientManager, serverPort);
        }
        return INSTANCE;
    }

    private ClientManager clientManager;
    private final int serverPort;

    private ExecutorService netManagerThreadPool = Executors.newCachedThreadPool();
    private ServerSocketListener serverSocketListener;

    private ObservableMap<ClientId, NetManager> clientsHolder = FXCollections.observableHashMap();

    private ServerNetComponent(ClientManager clientManager, int serverPort) {
        this.clientManager = clientManager;
        this.serverPort = serverPort;

        this.clientManager.setClientNetUpdate(this);
        this.clientsHolder.addListener(onClientChange);
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

    public void addClientSocket(Socket receiverSocket) {
        ClientId client = clientManager.addNewClient(receiverSocket.getInetAddress());

        NetManager netManager = new NetManager(receiverSocket, client);

        clientsHolder.put(client, netManager);
        netManagerThreadPool.execute(netManager);

        netManager.send(new LoginMessage(client));
    }

    private MapChangeListener<ClientId, NetManager> onClientChange = change -> {
        CommonNetMessageHandler<ChatMessage> chatMessageHandler = new CommonNetMessageHandler<>();
        chatMessageHandler.registerObserver(ServerChatComponent.get());
        change.getValueAdded().register(ChatMessage.class, chatMessageHandler);

    };

    @Override
    public void onNewMessageForClient(ClientId receiverClientId, NetMessage message) {
        clientsHolder.get(receiverClientId).send(message);
    }
}
