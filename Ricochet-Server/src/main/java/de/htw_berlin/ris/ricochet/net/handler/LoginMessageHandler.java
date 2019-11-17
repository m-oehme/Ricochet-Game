package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.ServerNetManager;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginMessageHandler implements NetMsgHandler<LoginMessage> {
    private static Logger log = LogManager.getLogger();

    private ServerNetManager serverNetManager;

    public LoginMessageHandler(ServerNetManager serverNetManager) {
        this.serverNetManager = serverNetManager;
    }

    @Override
    public Class<LoginMessage> getType() {
        return LoginMessage.class;
    }

    @Override
    public synchronized void handle(LoginMessage message) {
        log.info("New Client from: " + message.getInetAddress().toString());
        serverNetManager.addClient(message.getInetAddress());
    }
}
