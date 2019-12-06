package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatMessageHandler extends NetMessageHandlerImpl<ChatMessage, ChatMessageObserver> {
    private static Logger log = LogManager.getLogger();

    @Override
    public Class<ChatMessage> getType() {
        return ChatMessage.class;
    }

    @Override
    public synchronized void handle(ChatMessage message) {
        log.info("Chat Message from: " + message.getClientId());
        messageObserverHashMap.values().forEach(chatMessageObserver -> {
            chatMessageObserver.onNewMessage(message);
        });
    }
}
