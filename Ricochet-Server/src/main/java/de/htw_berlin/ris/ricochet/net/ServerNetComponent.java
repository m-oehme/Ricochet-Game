package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.client.ClientNetUpdate;
import de.htw_berlin.ris.ricochet.chat.ServerChatComponent;
import de.htw_berlin.ris.ricochet.net.handler.CommonNetMessageHandler;
import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.manager.NetManager;
import de.htw_berlin.ris.ricochet.net.manager.NetworkEvent;
import de.htw_berlin.ris.ricochet.net.manager.ServerSocketListener;
import de.htw_berlin.ris.ricochet.net.message.general.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.general.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.net.message.world.WorldMessage;
import de.htw_berlin.ris.ricochet.world.GameWorldComponent;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetComponent implements ClientNetUpdate, NetworkEvent {
    private static Logger log = LogManager.getLogger();

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
        netManager.setNetworkEvent(this);

        clientsHolder.put(client, netManager);
        netManagerThreadPool.execute(netManager);

        netManager.send(new LoginMessage(client));
    }

    private MapChangeListener<ClientId, NetManager> onClientChange = change -> {
        if (change.wasAdded()) {
            CommonNetMessageHandler<ChatMessage> chatMessageHandler = new CommonNetMessageHandler<>();
            chatMessageHandler.registerObserver(ServerChatComponent.get());
            change.getValueAdded().register(ChatMessage.class, chatMessageHandler);

            CommonNetMessageHandler<WorldMessage> worldMessageCommonNetMessageHandler = new CommonNetMessageHandler<>();
            worldMessageCommonNetMessageHandler.registerObserver(GameWorldComponent.get());
            change.getValueAdded().register(WorldMessage.class, worldMessageCommonNetMessageHandler);
        }
    };

    @Override
    public void onNewMessageForClient(ClientId receiverClientId, NetMessage message) {
        NetManager netManager = clientsHolder.get(receiverClientId);
        if (netManager != null){
            netManager.send(message);
        }
    }

    @Override
    public void onNetworkEvent(NetManager netManager, ClientId clientId, String event) {
        if (event.equals("LOGOUT")) {
            log.info("Logout from Client with ID: " + clientId);
            clientsHolder.remove(clientId);
            clientManager.removeClient(clientId);

            GameWorldComponent.get().removeAllObjectsForPlayer(clientId);
        }
    }
}
