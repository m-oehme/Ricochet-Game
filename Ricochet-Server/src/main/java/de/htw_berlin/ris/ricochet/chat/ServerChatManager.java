package de.htw_berlin.ris.ricochet.chat;

import de.htw_berlin.ris.ricochet.ClientManager;
import de.htw_berlin.ris.ricochet.net.handler.ChatMessageObserver;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;

import java.util.concurrent.LinkedBlockingQueue;

public class ServerChatManager implements Runnable, ChatMessageObserver {

    private boolean isRunning = true;

    private ClientManager clientManager;
    private LinkedBlockingQueue<ChatMessage> chatMesssageQueue = new LinkedBlockingQueue<>();

    public ServerChatManager(ClientManager clientManager) {
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
