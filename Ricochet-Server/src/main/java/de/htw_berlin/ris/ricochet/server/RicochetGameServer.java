package de.htw_berlin.ris.ricochet.server;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.server.net.handler.SimpleTextMessageHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RicochetGameServer {

    public static void main(String[] args) {
        NetManager manager = null;
        try {
            manager = new NetManager(InetAddress.getLocalHost(), 8080, 8080);
            manager.register(new SimpleTextMessageHandler());
            manager.startServer();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
