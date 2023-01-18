package BlackJack;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/*
 * Eduardo Boanca
 * Calcolatrice Multithread
 */

public class ServerJack {

    private final static int PORT = 7777;
    private static ArrayList<ClientHandlerJack> clients = new ArrayList<ClientHandlerJack>();
    private static ExecutorService pool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        
        while(true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket client = listener.accept();
            System.out.println("[SERVER] Client Connected");
            ClientHandlerJack clientThread = new ClientHandlerJack(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
    }
}