package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.ServerNetManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RicochetGameServer {
    private static Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        ClientManager clientManager = new ClientManager();
        ServerNetManager serverNetManager = new ServerNetManager(clientManager,8080,8081);
        serverNetManager.startServer();
    }
}
