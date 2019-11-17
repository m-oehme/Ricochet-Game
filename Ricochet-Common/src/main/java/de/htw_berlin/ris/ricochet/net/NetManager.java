package de.htw_berlin.ris.ricochet.net;

import de.htw_berlin.ris.ricochet.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.net.message.IpMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class NetManager implements Runnable {

    private Socket receiverSocket;
    private final InetAddress clientInetAddress;
    private final Integer clientPort;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);

    private NetworkReceiver networkReceiver = new NetworkReceiver();

    private HashMap<Class<? extends NetMessage>, NetMsgHandler> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();

    public NetManager() {
        this(null, null);
    }

    public NetManager(InetAddress clientInetAddress, Integer clientPort) {
        this.clientInetAddress = clientInetAddress;
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        netManagerThreadPool.execute(receivedMessageQuery);
    }

    public void stopServer() {
        receivedMessageQuery.setRunning(false);
    }

    public void register(NetMsgHandler<? extends NetMessage> netMsgHandler) {
        messageHandlerHolder.put(netMsgHandler.getType(), netMsgHandler);
    }

    public void send(NetMessage netMessage) {
        try {
            Socket clientSocket = new Socket(clientInetAddress, clientPort);

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(netMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiverSocket(Socket receiverSocket) {
        this.receiverSocket = receiverSocket;
        netManagerThreadPool.execute(networkReceiver);
    }

    class NetworkReceiver implements Runnable {

        @Override
        public void run() {
            try {
                waitForMessage();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void waitForMessage() throws IOException, ClassNotFoundException {
            if (clientInetAddress == null || clientInetAddress == receiverSocket.getInetAddress()) {
                ObjectInputStream in = new ObjectInputStream(receiverSocket.getInputStream());
                NetMessage message = (NetMessage) in.readObject();

                if (message instanceof IpMessage) {
                    ((IpMessage) message).setInetAddress(receiverSocket.getInetAddress());
                }

                if (messageHandlerHolder.containsKey(message.getClass())) {
                    receivedMessageQueue.offer(message);
                }
            }
            receiverSocket = null;
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
