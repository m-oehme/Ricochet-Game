package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private static Logger log = LogManager.getLogger();

    private List<ClientId> clientList = new ArrayList<>();

    private ClientNetUpdate clientNetUpdate;

    public void setClientNetUpdate(ClientNetUpdate clientNetUpdate) {
        this.clientNetUpdate = clientNetUpdate;
    }

    public ClientId addNewClient(InetAddress clientInetAddress) {
        ClientId clientId = new ClientId(clientInetAddress);
        log.info("New Client with ID: " + clientId);
        clientList.add(clientId);

        return clientId;
    }

    public void sendMessageToClients(ScopedMessage message) {
        sendMessageToClients(message, null);
    }

    public void sendMessageToClients(ScopedMessage message, List<ClientId> receiverClients) {

        switch (message.getMessageScope()) {
            case SELF:
                clientNetUpdate.onNewMessageForClient(message.getClientId(), message);
                break;
            case EVERYONE:
                clientList.forEach(clientId -> {
                    clientNetUpdate.onNewMessageForClient(clientId, message);
                });
                break;
            case EXCEPT_SELF:
                clientList.stream().filter(clientId -> !clientId.getClientId().equals(message.getClientId().getClientId())).forEach(clientId -> {
                    clientNetUpdate.onNewMessageForClient(clientId, message);
                });
                break;
            case CLIENT:
                receiverClients.stream().filter(receiver -> clientList.contains(receiver)).forEach(clientId -> {
                    clientNetUpdate.onNewMessageForClient(clientId, message);
                });
                break;
        }

    }
}
