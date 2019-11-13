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

    private final InetAddress sendAddress;
    private final int sendPort;
    private final int receivePort;

    private NetworkReceiver networkReceiver = new NetworkReceiver();

    private HashMap<Class<? extends NetMessage>, NetMsgHandler> messageHandlerHolder = new HashMap<>();
    private LinkedBlockingQueue<NetMessage> receivedMessageQueue = new LinkedBlockingQueue<>();
    private ReceivedMessageQuery receivedMessageQuery = new ReceivedMessageQuery();

    public NetManager(InetAddress sendAddress, int sendPort, int receivePort) {
        this.sendAddress = sendAddress;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
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
            Socket socket = new Socket(sendAddress, sendPort);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(netMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NetworkReceiver implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            try(ServerSocket serverSocket = new ServerSocket(receivePort)) {
                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();

                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    NetMessage message = (NetMessage) in.readObject();

                    receivedMessageQueue.offer(message);
                }
            } catch (IOException | ClassNotFoundException e) {
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
