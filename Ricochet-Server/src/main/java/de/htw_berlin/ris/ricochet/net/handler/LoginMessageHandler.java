package de.htw_berlin.ris.ricochet.server.net.handler;

import de.htw_berlin.ris.ricochet.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.server.net.ServerNetManager;


public class LoginMessageHandler implements NetMsgHandler<LoginMessage> {


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
        serverNetManager.addNetManagerForClient(message.getInetAddress());
        System.out.println("Hello I am: " + message.getInetAddress().toString());
    }
}
