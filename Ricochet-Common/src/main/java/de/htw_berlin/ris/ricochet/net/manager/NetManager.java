package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.net.handler.HandlerObserver;
import de.htw_berlin.ris.ricochet.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.net.message.LoginMessage;
import de.htw_berlin.ris.ricochet.net.message.NetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class NetManager implements Runnable {
    private static Logger log = LogManager.getLogger();

    private Socket receiverSocket = null;
    private final InetAddress clientInetAddress;
    private final Integer clientPort;
    private ClientId clientId;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);

    private NetworkReceiver networkReceiver = new NetworkReceiver();

    private HashMap<Class<? extends NetMessage>, NetMsgHandler> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();

    public NetManager() {
        this(null, null);
    }

    public NetManager(InetAddress clientInetAddress, Integer clientPort) {
        this(clientInetAddress, clientPort, null);
    }

    public NetManager(InetAddress clientInetAddress, Integer clientPort, ClientId clientId) {
        this.clientInetAddress = clientInetAddress;
        this.clientPort = clientPort;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        netManagerThreadPool.execute(receivedMessageQuery);
        netManagerThreadPool.execute(networkReceiver);
    }

    public void stopServer() {
        receivedMessageQuery.setRunning(false);
        networkReceiver.stop();
        netManagerThreadPool.shutdown();
    }

    public NetMsgHandler register(NetMsgHandler<? extends NetMessage, ? extends HandlerObserver> netMsgHandler) {
        return messageHandlerHolder.put(netMsgHandler.getType(), netMsgHandler);
    }

    public void unregister(NetMsgHandler<? extends NetMessage, ? extends HandlerObserver> netMsgHandler) {
        messageHandlerHolder.remove(netMsgHandler.getType());
    }

    public NetMsgHandler getRegisteredHandler(Class<? extends NetMessage> netMessageClass) {
        return messageHandlerHolder.get(netMessageClass);
    }

    public void send(NetMessage netMessage) {
        try {
            Socket clientSocket = new Socket(clientInetAddress, clientPort);

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(netMessage);
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            log.error("Cannot sent message: " + e.getMessage(), e);
        }
    }

    public void processSocket(Socket receiverSocket) {
        this.receiverSocket = receiverSocket;
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
            if (receiverSocket != null) {
                ObjectInputStream in = new ObjectInputStream(receiverSocket.getInputStream());
                NetMessage message = (NetMessage) in.readObject();

                if (clientId == null || clientId == message.getClientId()) {

                    if (message instanceof LoginMessage) {
                        ((LoginMessage) message).setInetAddress(receiverSocket.getInetAddress());
                    }

                    if (messageHandlerHolder.containsKey(message.getClass())) {
                        receivedMessageQueue.offer(message);
                    }
                }
                in.close();
                receiverSocket.close();
                receiverSocket = null;
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() {
            isRunning = false;
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
