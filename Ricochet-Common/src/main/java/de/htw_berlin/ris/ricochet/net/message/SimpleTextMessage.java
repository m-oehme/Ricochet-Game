package de.htw_berlin.ris.ricochet.net.message;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;

public class SimpleTextMessage extends ClientIdMessage implements NetMessage {
    private String textMessage = "TEST.TEST";

    public SimpleTextMessage(ClientId clientId, MessageScope messageScope) {
        super(clientId, messageScope);
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

}
