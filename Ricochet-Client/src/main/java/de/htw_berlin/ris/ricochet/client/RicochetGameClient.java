package de.htw_berlin.ris.ricochet.client;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.common.net.message.ExampleMsg;

public class RicochetGameClient {

    public static void main(String[] args) {
        NetManager.send(new ExampleMsg());
    }
}
