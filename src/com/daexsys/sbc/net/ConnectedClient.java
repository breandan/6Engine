package com.daexsys.sbc.net;

import com.daexsys.ijen3D.net.server.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient implements Runnable {
    private Socket socket;

    public ConnectedClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        sendChunks();

        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendChunks() {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sendChunk(dataOutputStream, i, 0, j);
            }
        }
    }

    public void sendChunk(DataOutputStream dataOutputStream, int x, int y, int z) {
        try {
            dataOutputStream.writeByte(0);
            dataOutputStream.writeInt(x);
            dataOutputStream.writeInt(y);
            dataOutputStream.writeInt(z);

            dataOutputStream.writeShort(3840);
            dataOutputStream.writeByte(0);
            dataOutputStream.writeShort(256);
            dataOutputStream.writeByte(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
