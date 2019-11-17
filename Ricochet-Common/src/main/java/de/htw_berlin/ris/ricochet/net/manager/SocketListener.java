package de.htw_berlin.ris.ricochet.net.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

abstract class SocketListener implements Runnable {
    private static Logger log = LogManager.getLogger();

    private boolean isRunning = true;
    private ServerSocket serverSocket;

    public SocketListener(int port) {
        try {
            this.serverSocket = configureServerSocket(port);

            log.info("Server listening to port: " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Cannot register Server Socket: " + e.getMessage());
            e.printStackTrace();
        }
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

    private ServerSocket configureServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

    private void waitForMessage() throws IOException {
        Socket receiveSocket = serverSocket.accept();
        log.debug("Message Received from: " + receiveSocket.toString());
        onMessageReceived(receiveSocket);
    }

    abstract protected void onMessageReceived(Socket receiveSocket);

}
