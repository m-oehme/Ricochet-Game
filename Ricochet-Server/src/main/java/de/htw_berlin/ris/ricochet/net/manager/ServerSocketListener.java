package de.htw_berlin.ris.ricochet.net.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSocketListener implements Runnable {
    private static Logger log = LogManager.getLogger();

    private boolean isRunning = true;
    private ServerSocket serverSocket;
    private ServerNetManager serverNetManager;

    ServerSocketListener(ServerNetManager serverNetManager, int port) {
        try {
            this.serverSocket = new ServerSocket(port);

            log.info("Server listening to port: " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Cannot register Server Socket: " + e.getMessage());
            e.printStackTrace();
        }
        this.serverNetManager = serverNetManager;
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
        log.debug("New Socket Connection: " + receiveSocket.toString());

        onNewSocketConnection(receiveSocket);
    }

    private void onNewSocketConnection(Socket receiveSocket) {
        serverNetManager.addClientSocket(receiveSocket);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

}
