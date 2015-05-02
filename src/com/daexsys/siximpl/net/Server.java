package com.daexsys.siximpl.net;

import com.daexsys.siximpl.world.Universe;
import com.daexsys.siximpl.world.planet.Planet;
import com.daexsys.siximpl.world.planet.PlanetType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    /**
     * The entire universe of the server
     */
    private static Universe serverUniverse = new Universe();

    /**
     * The spawn planet
     */
    private static Planet spawnPlanet;

    public static List<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();

    public static void startServer() {
        Planet planet = new Planet(new Random().nextInt(), PlanetType.GRASSY);
        spawnPlanet = planet;

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

    public static Planet getSpawnPlanet() {
        return spawnPlanet;
    }
}
