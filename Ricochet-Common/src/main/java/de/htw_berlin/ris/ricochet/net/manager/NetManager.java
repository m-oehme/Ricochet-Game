package de.htw_berlin.ris.ricochet.net.manager;

import de.htw_berlin.ris.ricochet.net.handler.NetMessageHandler;
import de.htw_berlin.ris.ricochet.net.message.general.LoginMessage;
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

    private ClientId clientId;

    private Socket socket;
    private ObjectOutputStream outputStream;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);

    private NetworkReceiver networkReceiver;

    private HashMap<Class<? extends NetMessage>, NetMessageHandler<? extends NetMessage>> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();


    public NetManager(Socket socket, ClientId clientId) {
        try {
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.networkReceiver = new NetworkReceiver(socket, new ObjectInputStream(socket.getInputStream()));
        } catch (IOException e) {
            log.error("Cannot create Output Socket: " + e.getMessage(), e);
        }
        this.clientId = clientId;
    }

    public NetManager(InetAddress clientInetAddress, Integer clientPort) {
        try {
            this.socket = new Socket(clientInetAddress, clientPort);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.networkReceiver = new NetworkReceiver(socket, new ObjectInputStream(socket.getInputStream()));
        } catch (IOException e) {
            log.error("Cannot create Socket Connection: " + e.getMessage(), e);
        }
        this.clientId = null;
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

    public <T extends NetMessage> NetMessageHandler<? extends NetMessage> register(Class<T> messageType, NetMessageHandler<T> netMessageHandler) {
        return messageHandlerHolder.put(messageType, netMessageHandler);
    }

    public <T extends NetMessage> void unregister(Class<T> messageType) {
        messageHandlerHolder.remove(messageType);
    }

    public NetMessageHandler getRegisteredHandler(Class<? extends NetMessage> netMessageClass) {
        return messageHandlerHolder.get(netMessageClass);
    }

    public void send(NetMessage netMessage) {
        try {
            outputStream.writeObject(netMessage);
        } catch (IOException e) {
            log.error("Cannot sent message: " + e.getMessage(), e);
        }
    }

    class NetworkReceiver implements Runnable {
        private boolean isRunning = true;

        private Socket receiverSocket;
        private ObjectInputStream inputStream;

        public NetworkReceiver(Socket receiverSocket, ObjectInputStream inputStream) {
            this.receiverSocket = receiverSocket;
            this.inputStream = inputStream;
        }

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

        private synchronized void waitForMessage() throws IOException, ClassNotFoundException {
            if (inputStream != null) {
                NetMessage message = (NetMessage) inputStream.readObject();

                if (clientId == null || clientId.compareTo(message.getClientId()) == 0) {

                    if (message instanceof LoginMessage) {
                        ((LoginMessage) message).setInetAddress(receiverSocket.getInetAddress());
                    }

                    if (messageHandlerHolder.containsKey(message.getClass()) || messageHandlerHolder.containsKey(message.getClass().getSuperclass())) {
                        receivedMessageQueue.offer(message);
                    }
                }
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

                    NetMessageHandler handler = messageHandlerHolder.get(message.getClass());

                    if (handler == null) {
                        handler = messageHandlerHolder.get(message.getClass().getSuperclass());
                    }

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
