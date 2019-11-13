package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.SimpleTextMessage;


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
