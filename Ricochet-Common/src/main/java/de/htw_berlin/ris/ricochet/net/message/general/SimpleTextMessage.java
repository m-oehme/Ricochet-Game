package de.htw_berlin.ris.ricochet.net.message.general;

import de.htw_berlin.ris.ricochet.net.manager.ClientId;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import de.htw_berlin.ris.ricochet.net.message.ScopedMessage;

public class SimpleTextMessage extends ScopedMessage implements NetMessage {
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
