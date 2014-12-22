package com.daexsys.sbc.net.server;

import com.daexsys.ijen3D.net.Packet;
import com.daexsys.sbc.net.EncodeChunk;
import com.daexsys.sbc.world.chunk.Chunk;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendChunkPacket implements Packet {
    public static byte PACKET_NUMBER = 20;

    private Chunk chunk;

    public SendChunkPacket(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        // Send the packet ID.
        dataOutputStream.writeByte(PACKET_NUMBER);

        // Send the contents of the packet, a compressed chunk.
        new EncodeChunk(chunk, dataOutputStream).encode();
    }
}
