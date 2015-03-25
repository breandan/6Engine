package com.daexsys.sbc.net.client;

import com.daexsys.sbc.SBC;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public Client() {}

    public void connect(String ip, int port) {
        try {
            final Socket socket = new Socket(ip, port);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                            // Chunk data
                            if(dataInputStream.readByte() == 0) {
                                // Chunk coords
                                int x = dataInputStream.readInt();
                                int y = dataInputStream.readInt();
                                int z = dataInputStream.readInt();

                                int block = 0;
                                Chunk chunk = new Chunk(x, y, z);

                                while(block < 4096) {
                                    int amount = dataInputStream.readShort();
                                    int type = dataInputStream.readByte();

                                    int area = 0;
                                    while(area < amount) {
                                        int inner = block % 256;
                                        Block toPut = type == 0 ? Block.DIRT : Block.GRASS;
                                        chunk.setNoBlock(inner % 16, block / 256, inner / 16, toPut);
                                        area++;
                                        block++;
                                    }
                                }

                                SBC.getUniverse().getPlanetAt(0, 0, 0).addChunk(chunk);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//                                    int code = dataInputStream.readInt();
//                                    System.out.println(code);
//                                    Block toPut = code == 0 ? Block.DIRT : Block.GRASS;
//
//                                    int inner = block % 256;
//                                    chunk.setBlock(inner % 16, block / 256, inner / 16, toPut);

}
