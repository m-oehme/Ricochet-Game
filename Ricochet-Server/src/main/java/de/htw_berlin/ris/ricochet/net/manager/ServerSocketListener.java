package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.net.ServerNetComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketListener implements Runnable {
    private static Logger log = LogManager.getLogger();

    private boolean isRunning = true;
    private ServerSocket serverSocket;
    private ServerNetComponent serverNetComponent;

    public ServerSocketListener(ServerNetComponent serverNetComponent, int port) {
        try {
            this.serverSocket = new ServerSocket(port);

            log.info("Server listening to port: " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("Cannot register Server Socket: " + e.getMessage());
            e.printStackTrace();
        }
        this.serverNetComponent = serverNetComponent;
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
        serverNetComponent.addClientSocket(receiveSocket);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }

}
