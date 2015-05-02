package com.daexsys.siximpl;

import com.daexsys.siximpl.net.Server;
import com.daexsys.siximpl.world.Universe;

public class ServerGame {
    private static Universe serverUniverse;

    /**
     * The SixEngine server.
     * @param args no arguments
     */
    public static void main(String[] args) {
        // Start server thread. This is where connections are accepted.
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Server.startServer();
            }
        });
        serverThread.setName("6Engine Server Thread");
        serverThread.start();

        serverUniverse = new Universe();
        SixEngineClient.universe = getServerUniverse();

        System.out.println("[6Engine] Server started");
    }

    public static Universe getServerUniverse() {
        return serverUniverse;
    }
}
