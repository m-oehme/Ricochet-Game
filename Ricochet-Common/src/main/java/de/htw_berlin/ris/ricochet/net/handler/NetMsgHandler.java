package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;

public interface NetMsgHandler<T extends NetMessage> {

    Class<T> getType();

    void handle(T message);
}
