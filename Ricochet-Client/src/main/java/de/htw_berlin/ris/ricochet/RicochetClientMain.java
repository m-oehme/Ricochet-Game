package de.htw_berlin.ris.ricochet;

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
    }
}
