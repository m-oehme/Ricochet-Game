package de.htw_berlin.ris.ricochet.client;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.common.net.message.SimpleTextMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RicochetGameClient {

    public static void main(String[] args) {
        NetManager manager = null;
        try {
            manager = new NetManager(InetAddress.getLocalHost(), 8080, 8080);
            manager.send(new SimpleTextMessage());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
