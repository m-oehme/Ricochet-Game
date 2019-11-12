package de.htw_berlin.ris.ricochet.common.net.handler;

import de.htw_berlin.ris.ricochet.common.net.message.NetMsg;

public interface NetMsgHandler<T extends NetMsg> {

    T getType();

    void handle(T message);

}
