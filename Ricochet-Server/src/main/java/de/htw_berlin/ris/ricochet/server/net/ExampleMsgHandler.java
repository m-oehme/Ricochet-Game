package de.htw_berlin.ris.ricochet.server.net;

import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.ExampleMsg;


public class ExampleMsgHandler implements NetMsgHandler<ExampleMsg> {
    @Override
    public Class<ExampleMsg> getType() {
        return ExampleMsg.class;
    }

    @Override
    public void handle(ExampleMsg message) {
        System.out.println(message.getExampleMessage());
    }
}
