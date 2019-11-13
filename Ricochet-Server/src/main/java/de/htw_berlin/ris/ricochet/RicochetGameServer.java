package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.ServerNetManager;

public class RicochetGameServer {


    public static void main(String[] args) {
        ServerNetManager serverNetManager = new ServerNetManager(8080,8081);

        serverNetManager.startNetManger();
    }
}
