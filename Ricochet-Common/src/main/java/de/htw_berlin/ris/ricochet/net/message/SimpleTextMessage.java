package de.htw_berlin.ris.ricochet.net.message;

public class SimpleTextMessage extends ClientIdMessage implements NetMessage {
    private String textMessage = "TEST.TEST";

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

}
