package com.daexsys.sbc.server;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.ijen3D.net.server.Server;

public class SBGServer {
    public static void startServer() {
        Server.start();
    }

    public static void addEntity(Entity entity) {
        Server.getEntityGroup().addEntity(entity);
    }

    public static void removeEntity(Entity entity) {
        Server.getEntityGroup().removeEntity(entity);
    }
}
