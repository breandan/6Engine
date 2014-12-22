package com.daexsys.sbc.net;

import com.daexsys.ijen3D.net.server.Server;
import com.daexsys.sbc.SBC;
import com.daexsys.sbc.net.server.listeners.ClientConnectionHandler;
import com.daexsys.sbc.world.Universe;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SBGServer {
    private static Universe universe;
    private static List<SBGConnection> connectionList = new ArrayList<SBGConnection>();

    public static void startServer() {
        universe = new Universe();
        Server.start();

        Server.addClientConnectionListener(new ClientConnectionHandler());
    }

    public static Universe getUniverse() {
        return universe;
    }

    public static void addSBGConnection(SBGConnection sbgConnection) {
        connectionList.add(sbgConnection);
    }
}
