package com.daexsys.siximpl.net;

import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.planet.Planet;
import com.daexsys.siximpl.world.planet.PlanetType;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

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

                if (packetNum == 5) {
                    int x = dataInputStream.readInt();
                    int y = dataInputStream.readInt() * -1;
                    int z = dataInputStream.readInt();
//                    System.out.println(x + " " + y + " " + z);

                    int range = 5;
                    for (int i = x - range; i < x + range; i++) {
                        for (int j = y - range; j < y + range; j++) {
                            for (int k = z - range; k < z + range; k++) {
                                if(i > 0 && j > -1 && k > 0) {
                                    if (planet.getChunk(i, j, k) == null) {
                                        Chunk chunk = generateChunk(i, j, k);
                                        sendChunk(dataOutputStream, chunk);
                                        planet.addChunk(chunk);
                                    }
                                }

                            }
                        }
                    }
                }
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

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 6; k++) {
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

            int total = Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;
            int layer = Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE;

            for (int point = 0; point < total; point++) {
                int inner = point % layer;
                int bx = inner % Chunk.CHUNK_SIZE;
                int by = point / layer;
                int bz = inner / Chunk.CHUNK_SIZE;

                currentBlock = chunk.getBlock(bx, by, bz);

                if(!started) {
                    if(currentBlock != setBlock) {

                        dataOutputStream.writeShort(amount);
                        dataOutputStream.writeByte(id);

                        if(currentBlock == null) {
                            id = 0;
                        } else {
                            id = currentBlock.getID();
                        }

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

                if(point == 4095) {
                    if(currentBlock == null) id = 0;
                    else
                    id = currentBlock.getID();
                }
            }

                if (amount > 0) {
                    dataOutputStream.writeShort(amount);
                    dataOutputStream.writeByte(id);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chunk generateChunk(int x, int y, int z) {
        Chunk chunk = new Chunk(x, y, z);

        Random random = new Random();
        for (int bx = 0; bx < Chunk.CHUNK_SIZE; bx++) {
            for (int bz = 0; bz < Chunk.CHUNK_SIZE; bz++) {
                boolean here = new Random().nextInt(Chunk.CHUNK_SIZE) == 0;

                int height = getHeightAt(bx + (chunk.getChunkX() * Chunk.CHUNK_SIZE), 1, bz + z * Chunk.CHUNK_SIZE);
//                System.out.println(height);

                boolean tree = random.nextInt(75) == 0;
                for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                    if(y >= 0) {
                        if(i + (y*Chunk.CHUNK_SIZE) < height && i+(y*Chunk.CHUNK_SIZE) > height - 4) {
//                            System.out.println("setting: " + i + "dirt");
                            chunk.setInvisibleBlock(bx, i, bz, Block.DIRT);
                        }
                        else if (i+(y*Chunk.CHUNK_SIZE) == height) {
//                            System.out.println("setting: " + i + "grass");
                            chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
                        }
                        else if(i+(y*Chunk.CHUNK_SIZE) < height) {
                            chunk.setInvisibleBlock(bx, i, bz, Block.STONE);
                        }

                        else {
                            if(tree) {
                                if(i+(y*Chunk.CHUNK_SIZE) >= height && i+(y*Chunk.CHUNK_SIZE) < height + 6) {
                                    chunk.setInvisibleBlock(bx, i, bz, Block.WOOD);
                                }

                                else if(i+(y*Chunk.CHUNK_SIZE) >= height && i+(y*Chunk.CHUNK_SIZE) < height + 7) {
                                    chunk.setInvisibleBlock(bx, i, bz, Block.LEAVES);
                                }
                            }
                        }
                    }
                     else {
                        chunk.setInvisibleBlock(bx, i, bz, Block.STONE);
                    }
                }
            }
        }

        return chunk;
    }

    static long seed = new Random().nextLong();
    public int getHeightAt(int x, int y, int z) {
//        return 3;
        //25
        OctaveGenerator octaveGenerator = new SimplexOctaveGenerator(seed, 3);
        octaveGenerator.setScale(.02);
        return new Double(octaveGenerator.noise(x, z, 0.5, 0.5) * 15).intValue() + 21;
//        return new Random().nextInt(2);
    }
}
