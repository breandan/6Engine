package com.daexsys.siximpl;

import com.daexsys.siximpl.entity.Player;
import com.daexsys.siximpl.net.Server;
import com.daexsys.siximpl.world.Universe;

public class ServerGame {
    public static void main(String[] args) {

        SBC.isClient = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.startServer();
            }
        }).start();
        SBC.universe = new Universe();
        SBC.player = new Player(SBC.getUniverse().getPlanetAt(0, 0, 0), 0,0,0);
        System.out.println("Server started");
    }
}
