package de.htw_berlin.ris.ricochet.server.net.handler;

import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.SimpleTextMessage;


public class SimpleTextMessageHandler implements NetMsgHandler<SimpleTextMessage> {
    @Override
    public Class<SimpleTextMessage> getType() {
        return SimpleTextMessage.class;
    }

    @Override
    public void handle(SimpleTextMessage message) {
        System.out.println(message.getTextMessage());
    }
}
