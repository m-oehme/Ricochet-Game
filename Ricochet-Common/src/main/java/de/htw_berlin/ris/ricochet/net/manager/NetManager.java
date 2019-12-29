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
    private volatile ObjectOutputStream outputStream;

    private ExecutorService netManagerThreadPool = Executors.newFixedThreadPool(2);
    private ExecutorService messageHandlerThreadPool = Executors.newCachedThreadPool();

    private NetworkReceiver networkReceiver;

    private HashMap<Class<? extends NetMessage>, NetMessageHandler<? extends NetMessage>> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();

    private NetworkEvent networkEvent;

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
        if (receivedMessageQuery != null && networkReceiver != null) {
            netManagerThreadPool.execute(receivedMessageQuery);
            netManagerThreadPool.execute(networkReceiver);
        }
    }

    public void stopServer() {
        receivedMessageQuery.stop();
        networkReceiver.stop();
        messageHandlerHolder.values().forEach(NetMessageHandler::stop);
        messageHandlerThreadPool.shutdown();
        netManagerThreadPool.shutdown();
    }

    public <T extends NetMessage> NetMessageHandler<? extends NetMessage> register(Class<T> messageType, NetMessageHandler<T> netMessageHandler) {
        messageHandlerThreadPool.execute(netMessageHandler);
        return messageHandlerHolder.put(messageType, netMessageHandler);
    }

    public <T extends NetMessage> void unregister(Class<T> messageType) {
        messageHandlerHolder.remove(messageType).stop();
    }

    public NetMessageHandler getRegisteredHandler(Class<? extends NetMessage> netMessageClass) {
        return messageHandlerHolder.get(netMessageClass);
    }

    public synchronized void send(NetMessage netMessage) {
        try {
            if (!socket.isClosed()) outputStream.writeObject(netMessage);
        } catch (IOException | NullPointerException e) {
            log.error("Cannot sent message: " + e.getMessage(), e);
        }
    }

    private void notifyConnectionBroken() {
        try {
            outputStream.close();
            stopServer();
            socket.close();
        } catch (IOException e) {
            log.error("Cannot close socket: " + e.getMessage(), e);
        }
        if (networkEvent != null) networkEvent.onNetworkEvent(this, clientId, "LOGOUT");
    }

    public void setNetworkEvent(NetworkEvent networkEvent) {
        this.networkEvent = networkEvent;
    }

    class NetworkReceiver implements Runnable {
        private boolean isRunning = true;

        private Socket receiverSocket;
        private volatile ObjectInputStream inputStream;

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
            } catch (IOException e) {
                notifyConnectionBroken();
            } catch (ClassNotFoundException e) {
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
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Cannot close InputStream: " + e.getMessage(), e);
            }
            isRunning = false;
        }
    }

    class ReceivedMessageQuery implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                try {
                    processMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void processMessage() throws InterruptedException {
            NetMessage message = receivedMessageQueue.take();

            NetMessageHandler handler = messageHandlerHolder.get(message.getClass());

            if (handler == null) {
                handler = messageHandlerHolder.get(message.getClass().getSuperclass());
            }

            if (handler != null) {
                handler.handle(message);
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() {
            isRunning = false;
        }
    }

}
