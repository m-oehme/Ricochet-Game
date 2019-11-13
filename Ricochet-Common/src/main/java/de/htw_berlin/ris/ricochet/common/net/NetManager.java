package de.htw_berlin.ris.ricochet.common.net;

import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class NetManager {

    private final Socket clientSocket;
    private final ServerSocket serverSocket;

    private NetworkReceiver networkReceiver = new NetworkReceiver();

    private HashMap<Class<? extends NetMessage>, NetMsgHandler> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();

    public NetManager(Socket clientSocket, ServerSocket serverSocket) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        new Thread(networkReceiver).start();
        new Thread(receivedMessageQuery).start();
    }

    public void stopServer() {
        networkReceiver.setRunning(false);
        receivedMessageQuery.setRunning(false);
    }

    public void register(NetMsgHandler<? extends NetMessage> netMsgHandler) {
        messageHandlerHolder.put(netMsgHandler.getType(), netMsgHandler);
    }

    public void send(NetMessage netMessage) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(netMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NetworkReceiver implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            try {
                while (isRunning) {
                    waitForMessage();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void waitForMessage() throws IOException, ClassNotFoundException {
            Socket clientSocket = serverSocket.accept();

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            NetMessage message = (NetMessage) in.readObject();

            receivedMessageQueue.offer(message);
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }
    }

    class ReceivedMessageQuery implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            try {
                while (isRunning) {
                    NetMessage message = receivedMessageQueue.take();

                    NetMsgHandler handler = messageHandlerHolder.get(message.getClass());
                    if (handler != null) {
                        handler.handle(message);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }
    }
}
