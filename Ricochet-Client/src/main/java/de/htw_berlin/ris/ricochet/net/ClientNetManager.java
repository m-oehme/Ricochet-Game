package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.net.handler.SimpleTextMessageHandler;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientNetManager {

    private final int clientPort;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);
    private NetManager netManger;
    private ClientSocketListener clientSocketListener;

    public ClientNetManager(InetAddress serverAddress, int serverPort, int clientPort) {
        this.clientPort = clientPort;

        netManger = new NetManager(serverAddress, serverPort);
        netManger.register(new SimpleTextMessageHandler());
        netManagerThreadPool.execute(netManger);
    }

    public void sentLogin() {
        LoginMessage login = new LoginMessage();

        netManger.send(login);
    }

    public void startMessageReceiver() {

        clientSocketListener = new ClientSocketListener(this, this.clientPort);
        netManagerThreadPool.execute(clientSocketListener);
    }

    public NetManager getNetManger() {
        return netManger;
    }
}
