package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public class ChatMessage extends ScopedMessage implements NetMessage {
    private String chatUsername;
    private String chatMessage;

    public ChatMessage(ClientId clientId, MessageScope messageScope, String chatUsername, String chatMessage) {
        super(clientId, messageScope);
        this.chatUsername = chatUsername;
        this.chatMessage = chatMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getChatUsername() {
        return chatUsername;
    }
}
