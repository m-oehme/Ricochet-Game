package de.htw_berlin.ris.ricochet.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSocketListener implements Runnable {

    private ServerNetManager serverNetManager;
    private boolean isRunning = true;
    private ServerSocket serverSocket;

    public ServerSocketListener(ServerNetManager serverNetManager, int port) {
        this.serverNetManager = serverNetManager;
        try {
            this.serverSocket = configureServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ServerSocket configureServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                waitForMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForMessage() throws IOException {
        System.out.println("Listen to: " + serverSocket.toString());
        Socket receiveSocket = serverSocket.accept();
        serverNetManager.getLoginManager().setReceiverSocket(receiveSocket);
        serverNetManager.getClientsHolder().values().forEach(netManager -> {
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
