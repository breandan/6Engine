package com.daexsys.sbc.net;

import com.daexsys.ijen3D.net.server.Connection;
import com.daexsys.sbc.entity.Player;
import com.daexsys.sbc.net.server.SendChunkPacket;
import com.daexsys.sbc.world.chunk.Chunk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SBGConnection {
    private Connection connection;
    private Player player;

    public SBGConnection(Connection connection) {
        this.connection = connection;
    }

    public void handlePacket(byte id, DataInputStream dataInputStream) throws IOException {
        if(id == 120) {
            // Get the coordinates of the chunk that the client wants.
            int x = dataInputStream.readInt();
            int y = dataInputStream.readInt();
            int z = dataInputStream.readInt();

            DataOutputStream dataOutputStream = connection.getDataOutputStream();

            // Get chunk (temporarily just from 0,0,0).
            Chunk chunkToSend = SBGServer.getUniverse().getPlanetAt(0, 0, 0).getChunk(x, y, z);

            // Encode and send the chunk.
            new SendChunkPacket(chunkToSend).write(dataOutputStream);
        }
    }

    public void loadPlayer() {
//        player = new Player();
    }
}
