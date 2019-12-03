package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.SimpleTextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SimpleTextMessageHandler extends NetMessageHandlerImpl<SimpleTextMessage, SimpleTestMessageObserver> {
    private static Logger log = LogManager.getLogger();

    @Override
    public Class<SimpleTextMessage> getType() {
        return SimpleTextMessage.class;
    }

    @Override
    public void handle(SimpleTextMessage message) {
        log.info(message.getTextMessage());
    }
}
