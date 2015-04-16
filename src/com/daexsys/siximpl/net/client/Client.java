package com.daexsys.siximpl.net.client;

import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.siximpl.SBC;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The network client
 */
public class Client {
    public static Socket socket;
    public Client() {}

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);

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
                                final Chunk chunk = new Chunk(x, y, z);

                                while(block < 4096) {
                                    int amount = dataInputStream.readShort();
                                    int type = dataInputStream.readByte();

                                    System.out.println(x + " " + y + " " + z + " " + amount + " " + type);

                                    int area = 0;
                                    while(area < amount) {
                                        int inner = block % 256;
                                        Block toPut = type == 0 ? Block.AIR : (type == 1 ? Block.GRASS : type == 2 ? Block.DIRT : Block.STONE);
                                        chunk.setInvisibleBlock(inner % 16, block / 256, inner / 16, toPut);
                                        area++;
                                        block++;
                                    }
                                }

                                IjWindow.addRenderer(new Renderer() {
                                    @Override
                                    public void render() {
//                                        SBC.getUniverse().getPlanetAt(0,0,0)
                                                chunk.rebuildRenderGeometry();
                                        IjWindow.removeRenderer(this);
                                    }
                                });

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
