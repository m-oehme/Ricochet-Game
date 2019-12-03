package de.htw_berlin.ris.ricochet.net.message;

public class ChatMessage extends ClientIdMessage implements NetMessage {
    private String chatMessage;

    public ChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatMessage() {
        return chatMessage;
    }
}
