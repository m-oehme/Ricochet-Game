package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public class ChatMessage extends ScopedMessage implements NetMessage {
    private String chatMessage;

    public ChatMessage(ClientId clientId, MessageScope messageScope, String chatMessage) {
        super(clientId, messageScope);
        this.chatMessage = chatMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }
}
