package com.daexsys.siximpl.net;

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
            for (int j = -12; j < 3; j++) {
                for (int k = -3; k < 3; k++) {
                    sendChunk(dataOutputStream, i, k, j);
                }
            }
        }
    }

    public void sendChunk(DataOutputStream dataOutputStream, int x, int y, int z) {
        try {
            dataOutputStream.writeByte(0);
            dataOutputStream.writeInt(x);
            dataOutputStream.writeInt(y);
            dataOutputStream.writeInt(z);

            if(y > 2) {
                dataOutputStream.writeShort(4096);
                dataOutputStream.writeByte(60);
            } else if(y > 1) {
                dataOutputStream.writeShort(3840);
                dataOutputStream.writeByte(0);
                dataOutputStream.writeShort(256);
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeShort(4096);
                dataOutputStream.writeByte(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
