package com.daexsys.sbc.net.client;

import com.daexsys.ijen3D.net.Packet;
import org.omg.CORBA.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestChunkPacket implements Packet {
    public static byte PACKET_NUMBER = 120;

    private int x = 0;
    private int y = 0;
    private int z = 0;

    public RequestChunkPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        // Send packet number
        dataOutputStream.writeByte(PACKET_NUMBER);

        // Send coords of the requested packet.
        dataOutputStream.writeInt(x);
        dataOutputStream.writeInt(y);
        dataOutputStream.writeInt(z);
    }
}
