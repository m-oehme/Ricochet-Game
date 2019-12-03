package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.ChatMessage;

public interface ChatMessageObserver extends HandlerObserver {
    void onNewMessage(ChatMessage chatMessage);
}
