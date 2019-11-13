package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.net.handler.LoginMessageHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetManager {

    private final int serverPort;
    private final int clientPort;

    private ExecutorService netManagerThreadPool = Executors.newCachedThreadPool();
    private ServerSocketListener serverSocketListener = new ServerSocketListener();

    private NetManager loginManager;
    private HashMap<InetAddress, NetManager> clientsHolder = new HashMap<>();

    public ServerNetManager(int serverPort, int clientPort) {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void addNetManagerForClient(InetAddress clientInetAddress) {
        NetManager clientManager = new NetManager(clientInetAddress, clientPort);

        clientsHolder.put(clientInetAddress, clientManager);
        netManagerThreadPool.execute(clientManager);
    }

    public void startNetManger() {
        netManagerThreadPool.execute(serverSocketListener);

        loginManager = new NetManager();
        loginManager.register(new LoginMessageHandler(this));

        netManagerThreadPool.execute(loginManager);
    }

    class ServerSocketListener implements Runnable {

        private boolean isRunning = true;

        private ServerSocket serverSocket;

        public ServerSocketListener() {
            try {
                configureServerSocket(serverPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void configureServerSocket(int serverPort) throws IOException {
            serverSocket = new ServerSocket(serverPort);
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    waitForMessage();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void waitForMessage() throws IOException, ClassNotFoundException {
            Socket receiveSocket = serverSocket.accept();
            loginManager.setReceiverSocket(receiveSocket);
            clientsHolder.values().forEach(netManager -> {
                netManager.setReceiverSocket(receiveSocket);
            });
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() {
            isRunning = false;
        }
    }
}
