package de.htw_berlin.ris.ricochet.chat;

import de.htw_berlin.ris.ricochet.client.ClientManager;
import de.htw_berlin.ris.ricochet.net.handler.NetMessageObserver;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;

import java.util.concurrent.LinkedBlockingQueue;

public class ServerChatComponent implements Runnable, NetMessageObserver<ChatMessage> {

    private static ServerChatComponent INSTANCE = null;
    public static ServerChatComponent get() {
        return INSTANCE;
    }
    public static ServerChatComponent create(ClientManager clientManager) {
        if( INSTANCE == null ) {
            INSTANCE = new ServerChatComponent(clientManager);
        }
        return INSTANCE;
    }

    private boolean isRunning = true;

    private ClientManager clientManager;
    private LinkedBlockingQueue<ChatMessage> chatMesssageQueue = new LinkedBlockingQueue<>();

    private ServerChatComponent(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                processChatMessage(chatMesssageQueue.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewMessage(ChatMessage chatMessage) {
        chatMesssageQueue.offer(chatMessage);
    }

    synchronized void processChatMessage(ChatMessage chatMessage) {
        chatMessage.setMessageScope(MessageScope.EXCEPT_SELF);
        clientManager.sendMessageToClients(chatMessage);
    }
}
