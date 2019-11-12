package de.htw_berlin.ris.ricochet.common.net;

import de.htw_berlin.ris.ricochet.common.net.handler.NetMsgHandler;
import de.htw_berlin.ris.ricochet.common.net.message.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NetManager {

    private HashMap<Class<? extends NetMsg>, NetMsgHandler<? extends NetMsg>> messageHandlerHolder = new HashMap<>();
    private NetworkReceiver networkReceiver = new NetworkReceiver();

    public NetManager() {
        new Thread(networkReceiver).start();
    }

    public void register(NetMsgHandler<? extends NetMsg> netMsgHandler) {
        messageHandlerHolder.put(netMsgHandler.getType(), netMsgHandler);
    }

    public static void send(NetMsg netMsg) {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 8080);

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

                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());


                    Object obj = in.readObject();

                    if (obj instanceof ExampleMsg) {
                        ExampleMsg msg = (ExampleMsg) obj;

//                        messageHandlerHolder.get(ExampleMsg.class).handle(msg);
                    }
//                    for (Map.Entry<Class<? extends NetMsg>, NetMsgHandler<? extends NetMsg>> messageHandler : messageHandlerHolder.entrySet() ) {
//
//                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
