package com.daexsys.sbc.net.server.listeners;

import com.daexsys.ijen3D.net.server.events.ClientConnectionEvent;
import com.daexsys.ijen3D.net.server.listeners.ClientConnectionListener;
import com.daexsys.sbc.net.SBGConnection;
import com.daexsys.sbc.net.SBGServer;

public class ClientConnectionHandler implements ClientConnectionListener {
    @Override
    public void clientConnect(ClientConnectionEvent clientConnectionEvent) {
        SBGConnection sbgConnection = new SBGConnection(clientConnectionEvent.getClientConnection());
        SBGServer.addSBGConnection(sbgConnection);
    }
}
