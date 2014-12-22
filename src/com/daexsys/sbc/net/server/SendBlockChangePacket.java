package com.daexsys.sbc.net.server;

import com.daexsys.ijen3D.net.Packet;
import com.daexsys.sbc.BlockChange;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendBlockChangePacket implements Packet {
    public static byte PACKET_NUMBER = 21;
    private BlockChange blockChange;

    public SendBlockChangePacket(BlockChange blockChange) {
        this.blockChange = blockChange;
    }

    public static byte getPacketNumber() {
        return PACKET_NUMBER;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(PACKET_NUMBER);
        dataOutputStream.writeInt(blockChange.getX());
        dataOutputStream.writeInt(blockChange.getY());
        dataOutputStream.writeInt(blockChange.getZ());
        dataOutputStream.writeByte(blockChange.getNewID());
    }
}
