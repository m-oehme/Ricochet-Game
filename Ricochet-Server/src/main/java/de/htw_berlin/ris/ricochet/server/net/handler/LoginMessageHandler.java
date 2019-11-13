package de.htw_berlin.ris.ricochet.server.net.handler;

import de.htw_berlin.ris.ricochet.common.net.NetManager;
import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.common.net.message.SimpleTextMessage;
import de.htw_berlin.ris.ricochet.server.net.ServerNetManager;

import java.net.InetAddress;
import java.util.HashMap;


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
