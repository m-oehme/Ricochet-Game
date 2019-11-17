package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.NetManager;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;

import java.io.IOException;
import java.net.*;

public class RicochetGameClient {

    public static void main(String[] args) {
        try {
            ClientNetManager clientNetManager = new ClientNetManager(InetAddress.getLocalHost(), 8080, 8081);
            clientNetManager.startMessageReceiver();

            clientNetManager.sentLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
