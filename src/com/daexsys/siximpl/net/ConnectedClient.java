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

/**
 * Representation of a client's connection to the server
 */
public class ConnectedClient implements Runnable {
    // Network socket to the player
    private Socket socket;

    // The planet the player is on
    private Planet planet;

    // Logical planet representing the players chunks
    private Planet sentChunks;

    private DataOutputStream dataOutputStream;

    private DataInputStream dataInputStream;

    public ConnectedClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // If player doesn't have a planet, spawn them on the spawn planet
        if(planet == null) {
            planet = Server.getSpawnPlanet();
        }

        //"Logical planet" representing chunks currently loaded on the client
        sentChunks = new Planet(new Random().nextLong(), PlanetType.GRASSY);
//
        // Create DataInputStream and DataOutputStream for clients
        dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////

        // Begin player loop
        boolean continueLoop = true;
        while(continueLoop) {
            try {
                byte packetNum = dataInputStream.readByte();

                // Packet number 2 is the block set packet (Client -> Server)
                // It contains 4 ints: the block id, the X coord, the Y coord, and the Z coord
                if(packetNum == 2) {
                    int id = dataInputStream.readInt();
                    Block block = null;

                    if(id == 5) block = Block.STONE;

                    int x = dataInputStream.readInt();
                    int y = dataInputStream.readInt();
                    int z = dataInputStream.readInt();

                    planet.setBlock(x, y, z, block);

                    // Update block on other clients
                    for(ConnectedClient connectedClient : Server.connectedClients) {
                        if(connectedClient != this) {
                            DataOutputStream dataOutputStream1 = connectedClient.getDataOutputStream();

                            dataOutputStream1.writeByte(2);
                            dataOutputStream1.writeInt(id);
                            dataOutputStream1.writeInt(x);
                            dataOutputStream1.writeInt(y);
                            dataOutputStream1.writeInt(z);
                        }
                    }
                }

                // Packet number 5 is the player chunk load packet (Client -> Server)
                // It tells the server where the player is in the world and prompts a chunk send
                if (packetNum == 5) {
                    int x = dataInputStream.readInt();
                    int y = dataInputStream.readInt() * -1;
                    int z = dataInputStream.readInt();

                    // The range of how far away from the player chunks will load
                    int range = 5;

                    for (int i = x - range; i < x + range; i++) {
                        for (int j = y - range; j < y + range; j++) {
                            for (int k = z - range; k < z + range; k++) {
                                if(i > 0 && j > -3 && k > 0) {
                                    if (sentChunks.getChunk(i, j, k) == null) {
                                        Chunk chunk = planet.getChunk(i, j, k);

                                        if(chunk == null) {
                                            chunk = generateChunk(i, j, k);
                                            planet.addChunk(chunk);
                                        }

                                        sentChunks.addChunk(chunk);

                                        sendChunk(dataOutputStream, chunk);
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                // If an exception occurs, the player has presumably lost connection to the server
                continueLoop = false;
            }
        }

        System.out.println("[Log] Player disconnected");
    }

    /**
     * Compresses a chunk and sends it to the client.
     * @param dataOutputStream the client's DataOutputStream
     * @param chunk the chunk to be sent
     */
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

    /**
     * Performs world generation on a chunk. Needs to be refactored into a seperate sysetem.
     * @param x the X coordinate of the chunk
     * @param y the Y coordinate of the chunk
     * @param z the Z coordinate of the chunk
     * @return the chunk, now generated
     */
    public Chunk generateChunk(int x, int y, int z) {
        Chunk chunk = new Chunk(x, y, z);

        Random random = new Random();
        for (int bx = 0; bx < Chunk.CHUNK_SIZE; bx++) {
            for (int bz = 0; bz < Chunk.CHUNK_SIZE; bz++) {
                int height = getHeightAt(bx + (chunk.getChunkX() * Chunk.CHUNK_SIZE), 1, bz + z * Chunk.CHUNK_SIZE);

                boolean tree = random.nextInt(75) == 0;
                for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                    if(y >= 0) {
                        if(i + (y*Chunk.CHUNK_SIZE) < height && i+(y*Chunk.CHUNK_SIZE) > height - 4) {
                            chunk.setInvisibleBlock(bx, i, bz, Block.DIRT);
                        }
                        else if (i+(y*Chunk.CHUNK_SIZE) == height) {
                            if(i + (y* Chunk.CHUNK_SIZE) < 18) {
                                chunk.setInvisibleBlock(bx, i, bz, Block.SAND);
                            } else if(i + (y* Chunk.CHUNK_SIZE) < 15) {
                                chunk.setInvisibleBlock(bx, i, bz, Block.WATER);
                            } else {
                                chunk.setInvisibleBlock(bx, i, bz, Block.GRASS);
                            }
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

    public int getHeightAt(int x, int y, int z) {
//        long seed = planet.getSeed();

//        OctaveGenerator octaveGenerator = new SimplexOctaveGenerator(seed, 3);
//        octaveGenerator.setScale(.009);
//        return new Double(octaveGenerator.noise(x, z, 0.0001, 0.0001) * 10).intValue() + 21;
        return new Random().nextInt(2);
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }
}
