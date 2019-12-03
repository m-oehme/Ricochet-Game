package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


public class ChatMessageHandler implements NetMsgHandler<ChatMessage, ChatMessageObserver> {
    private static Logger log = LogManager.getLogger();

    private HashMap<Class<? extends ChatMessageObserver>, ChatMessageObserver> chatMessageObserverHashMap = new HashMap<>();

    public ChatMessageHandler() { }

    public ChatMessageHandler(ChatMessageObserver handlerObserver) {
        chatMessageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
    }

    @Override
    public Class<ChatMessage> getType() {
        return ChatMessage.class;
    }

    @Override
    public synchronized void handle(ChatMessage message) {
        log.info("Chat Message: " + message.getClientId());
        chatMessageObserverHashMap.values().forEach(chatMessageObserver -> {
            chatMessageObserver.onNewMessage(message);
        });
    }

    @Override
    public void registerObserver(ChatMessageObserver handlerObserver) {
        chatMessageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
    }

    @Override
    public void unregisterObserver(ChatMessageObserver handlerObserver) {
        chatMessageObserverHashMap.remove(handlerObserver.getClass());
    }
}
