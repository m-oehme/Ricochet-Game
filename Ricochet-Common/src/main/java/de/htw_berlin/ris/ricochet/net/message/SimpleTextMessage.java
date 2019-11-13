package de.htw_berlin.ris.ricochet.common.net.message;

public class SimpleTextMessage implements NetMessage {
    private String textMessage = "TEST.TEST";

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
