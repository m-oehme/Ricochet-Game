package de.htw_berlin.ris.ricochet.net.handler;

import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CommonNetMessageHandler<T extends NetMessage> implements NetMessageHandler<T> {
    private static Logger log = LogManager.getLogger();
    private boolean isRunning = true;

    protected ConcurrentHashMap<Class<? extends NetMessageObserver>, NetMessageObserver<T>> messageObserverHashMap = new ConcurrentHashMap<>();
    private LinkedBlockingQueue<T> receivedMessageQueue = new LinkedBlockingQueue<>();

    @Override
    public void handle(T message) {
        receivedMessageQueue.offer(message);
    }

    @Override
    public NetMessageHandler<T> registerObserver(NetMessageObserver<T> handlerObserver) {
        messageObserverHashMap.put(handlerObserver.getClass(), handlerObserver);
        return this;
    }

    @Override
    public void unregisterObserver(NetMessageObserver<T> handlerObserver) {
        messageObserverHashMap.remove(handlerObserver.getClass());
    }

    private void processMessage() throws InterruptedException {
        NetMessage msg = receivedMessageQueue.take();
        messageObserverHashMap.values().forEach(chatMessageObserver -> {
            chatMessageObserver.onNewMessage((T) msg);
        });

    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                processMessage();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void stop() {
        isRunning = false;
    }
}
