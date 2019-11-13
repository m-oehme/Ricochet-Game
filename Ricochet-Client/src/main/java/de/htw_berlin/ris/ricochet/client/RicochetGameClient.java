package de.htw_berlin.ris.ricochet.client;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.common.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.common.net.message.SimpleTextMessage;

import java.io.IOException;
import java.net.*;

public class RicochetGameClient {

    public static void main(String[] args) {
        LoginMessage login = new LoginMessage();

        NetManager manager = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8081);

            manager = new NetManager(InetAddress.getLocalHost(), 8080);
            manager.send(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
