package com.daexsys.sbc.net;

import com.daexsys.ijen3D.net.server.Server;
import com.daexsys.sbc.world.Universe;

import java.net.ServerSocket;

public class SBGServer {
    private static ServerSocket serverSocket;
    private static Universe universe;

    public static void startServer() {
        universe = new Universe();
        Server.start();
        serverSocket = Server.getServerSocket();
    }

    public static Universe getUniverse() {
        return universe;
    }
}
