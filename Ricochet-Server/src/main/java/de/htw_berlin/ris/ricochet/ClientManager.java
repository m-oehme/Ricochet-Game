package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.ClientIdMessage;
import de.htw_berlin.ris.ricochet.net.message.SimpleTextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private static Logger log = LogManager.getLogger();

    private List<ClientId> clientList = new ArrayList<>();

    private ClientNetUpdate clientNetUpdate;

    public void setClientNetUpdate(ClientNetUpdate clientNetUpdate) {
        this.clientNetUpdate = clientNetUpdate;
    }

    public void addNewClient(ClientId clientId) {
        log.info("New Client with ID: " + clientId);

        clientList.add(clientId);

        clientNetUpdate.onNewMessageForClient(clientId, new ClientIdMessage());
    }
}
