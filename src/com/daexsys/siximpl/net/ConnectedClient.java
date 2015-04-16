package com.daexsys.siximpl.net;

import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.planet.Planet;
import com.daexsys.siximpl.world.planet.PlanetType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class ConnectedClient implements Runnable {
    private Socket socket;
    private Planet planet;

    public ConnectedClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        planet = new Planet(new Random().nextLong(), PlanetType.GRASSY);
        sendChunks();

        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {
            try {
                byte packetNum = dataInputStream.readByte();

//                if (packetNum == 5) {
//                    int x = dataInputStream.readInt();
//                    int y = dataInputStream.readInt() * -1;
//                    int z = dataInputStream.readInt();
//                    System.out.println(x + " " + y + " " + z);
//
//                    for (int i = x - 2; i < x + 2; i++) {
//                        for (int j = y - 1; j < y + 1; j++) {
//                            for (int k = z - 2; k < z + 2; k++) {
//                                System.out.println(i + " " + j + " " + k);
//                                if(planet.getChunk(i, j, k) == null) {
//                                    Chunk chunk = generateChunk(i, j, k);
//                                    sendChunk(dataOutputStream, chunk);
//                                    planet.addChunk(chunk);
//                                }
//
//                            }
//                        }
//                    }
//                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
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
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 9; k++) {
                    Chunk chunk = generateChunk(i, j, k);
                    sendChunk(dataOutputStream, chunk);
                    planet.addChunk(chunk);
                }
            }
        }
    }

    public void sendChunk(DataOutputStream dataOutputStream, Chunk chunk) {
        int x = chunk.getChunkX();
        int y = chunk.getChunkY();
        int z = chunk.getChunkZ();

        try {
            dataOutputStream.writeByte(0);
            dataOutputStream.writeInt(x);
            dataOutputStream.writeInt(y);
            dataOutputStream.writeInt(z);

            Block setBlock = null;
            int amount = 0;
            short id = 0;
            boolean started = true;
            Block currentBlock = null;

            for (int point = 0; point < 4096; point++) {
                int inner = point % 256;
                int bx = inner % 16;
                int by = point / 256;
                int bz = inner / 16;

                currentBlock = chunk.getBlock(bx, by, bz);

                if(!started) {
                    if(currentBlock != setBlock) {
                        if(currentBlock == null) {
                            id = 0;
                        } else {
                            id = currentBlock.getID();
                        }

                        dataOutputStream.writeShort(amount);
                        dataOutputStream.writeByte(id);

                        setBlock = currentBlock;
                        amount = 1;
                    } else {
                        amount++;
                        if(currentBlock != null) {
                            id = currentBlock.getID();
                        }
                        else {
                            id = 0;
                        }
                    }
                } else {
                    started = false;
                    setBlock = currentBlock;
                    amount++;
                }
            }

//            if(currentBlock != null) {
                if (amount > 0) {
                    dataOutputStream.writeShort(amount);
                    dataOutputStream.writeByte(id);
                }
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chunk generateChunk(int x, int y, int z) {
        Chunk chunk = new Chunk(x, y, z);

        for (int bx = 0; bx < 16; bx++) {
            for (int bz = 0; bz < 16; bz++) {
                boolean here = new Random().nextInt(16) == 0;

                int height = getHeightAt(bx, 1, bz);

                for (int i = 0; i < 16; i++) {
//                    if(here) {
//                        if(i + y * 16 == height + 2) {
//                            chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
//                        }
//                    }
//
//                    if(i + y * 16 < height) {
//                    } else if(i + y * 16 == height) {
//                        chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
//                    } else {
////                        if(new Random().nextInt(512) == 0){
//                            chunk.setInvisibleBlock(bx, i, bz, null);
////                        } else {
////                            chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
////
////                        }
//                    }
                    if(y == 0) {
                        if(i < 14)
                            chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
                        else
                            chunk.setInvisibleBlock(bx, i, bz, Block.DIRT);
                    } else if(y > 0) {

                        chunk.setInvisibleBlock(bx, i, bz, null);
                    } else {
                        chunk.setInvisibleBlock(bx, i, bz, Block.STONE);
                    }
                }
            }
        }

        return chunk;
    }

    public int getHeightAt(int x, int y, int z) {
        return new Random().nextInt(y);
    }
}
