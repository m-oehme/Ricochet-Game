package de.htw_berlin.ris.ricochet.server;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.server.net.ExampleMsgHandler;

public class RicochetGameServer {

    public static void main(String[] args) {
        NetManager manager = new NetManager();
        manager.register(new ExampleMsgHandler());

        while (true) {

        }
    }

}
