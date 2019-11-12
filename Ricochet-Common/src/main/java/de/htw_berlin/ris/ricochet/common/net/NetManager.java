package de.htw_berlin.ris.ricochet.common.net;

import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.NetMsg;
import sun.nio.ch.ThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class NetManager {

    private HashMap<Class<? extends NetMsg>, NetMsgHandler<? extends NetMsg>> messageHandlerHolder = new HashMap<>();
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    public NetManager() {
        new Thread(networkReceiver).start();
    }

    public void register(NetMsgHandler<? extends NetMsg> netMsgHandler) {
        messageHandlerHolder.put(netMsgHandler.getType().getClass(), netMsgHandler);
    }

    public static void send(NetMsg netMsg) {
        try {
            Socket socket = new Socket("http://localhost", 8080);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(netMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NetworkReceiver implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            try(ServerSocket serverSocket = new ServerSocket(8080)) {
                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();

//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    Object obj = in.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
