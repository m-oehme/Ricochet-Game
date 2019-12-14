package de.htw_berlin.ris.ricochet;

import de.htw_berlin.ris.ricochet.net.manager.ClientNetManager;
import de.htw_berlin.ris.ricochet.net.message.ChatMessage;
import de.htw_berlin.ris.ricochet.net.message.MessageScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class RicochetClientMain {
    private static Logger log = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(args[0]);
        int serverPort = Integer.parseInt(args[1]);

        RicochetApplication.initialize(serverAddress, serverPort);

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nUsername: ");
        String username = scanner.next();
        RicochetGame.init();

        while (true) {
       /*     System.out.print("\nChat: ");
            String message = scanner.next();
            ClientNetManager.get().sentMessage(new ChatMessage(RicochetApplication.get().getClientId(), MessageScope.EXCEPT_SELF, username, message));*/
            RicochetGame.Run();
        }

    }
}
