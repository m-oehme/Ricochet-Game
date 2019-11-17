package de.htw_berlin.ris.ricochet.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSocketListener implements Runnable {
    private static Logger log = LogManager.getLogger();

    private ServerNetManager serverNetManager;
    private boolean isRunning = true;
    private ServerSocket serverSocket;

    public ServerSocketListener(ServerNetManager serverNetManager, int port) {
        this.serverNetManager = serverNetManager;
        try {
            this.serverSocket = configureServerSocket(port);

            log.info("Server listening to port: " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Cannot register Server Socket: " + e.getMessage());
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
        Socket receiveSocket = serverSocket.accept();
        log.info("Message Received from: " + receiveSocket.toString());
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
