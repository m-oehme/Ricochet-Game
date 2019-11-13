package de.htw_berlin.ris.ricochet.client;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.common.net.message.SimpleTextMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RicochetGameClient {

    public static void main(String[] args) {
        NetManager manager = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8080);

            manager = new NetManager(clientSocket, serverSocket);
            manager.send(new SimpleTextMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
