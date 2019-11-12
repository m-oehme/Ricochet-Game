package de.htw_berlin.ris.ricochet.common.net.message;

import java.io.Serializable;

public class ExampleMsg implements NetMsg {
    private String exampleMessage = "TEST.TEST";

    public String getExampleMessage() {
        return exampleMessage;
    }

    public void setExampleMessage(String exampleMessage) {
        this.exampleMessage = exampleMessage;
    }
}
