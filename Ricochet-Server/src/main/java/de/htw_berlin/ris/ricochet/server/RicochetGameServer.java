package de.htw_berlin.ris.ricochet.server;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.server.net.handler.SimpleTextMessageHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RicochetGameServer {

    public static void main(String[] args) {
        NetManager manager = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8080);

            manager = new NetManager(clientSocket, serverSocket);
            manager.register(new SimpleTextMessageHandler());
            manager.startServer();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
