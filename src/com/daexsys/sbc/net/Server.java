package com.daexsys.sbc.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(2171);

            while(true) {
                Socket socket = serverSocket.accept();
                ConnectedClient connectedClient = new ConnectedClient(socket);
                connectedClients.add(connectedClient);
                new Thread(connectedClient).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
