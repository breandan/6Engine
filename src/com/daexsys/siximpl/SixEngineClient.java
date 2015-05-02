package com.daexsys.siximpl;

import com.daexsys.depthz.TextureUtils;
import com.daexsys.ijen3D.Camera;
import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.ijen3D.entity.EntityGroup;
import com.daexsys.siximpl.entity.Player;
import com.daexsys.siximpl.net.client.Client;
import com.daexsys.siximpl.world.Universe;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

/**
 * The SixEngine Client.
 *
 * Currently, when started immediately attempts to connect to a server at 127.0.0.1 without prompt.
 *
 * Needs serious refactoring.
 */
public class SixEngineClient {
    // The network client thread itself.
    public static Client client;

    // The active player entity.
    public static Player player;

    // The universe (set of worlds) in the current game. Should only be serverside, eventually.
    public static Universe universe;

    // Object containing all entities that will be rendered / have logic performed on them
    public static EntityGroup entityGroup;

    /**
     * Main method to launch the client. There are no arguments.
     * Creates the Universe, the player entity, and spawns a connection to a server at 127.0.0.1.
     * The game will isntantly close if the server is not there.
     */
    public static void main(String[] args) {
        universe = new Universe();
        player = new Player(getUniverse().getPlanetAt(0, 0, 0), 0,0,0);

        entityGroup = new EntityGroup();

        client = new Client();
        client.connect("127.0.0.1", 2718);
        initClientGame();
    }

    /**
     * Method that creates the window and starts the renderer and client logic thread.
     */
    public static void initClientGame() {
        // Create the window.
        IjWindow.create("6Engine", new DisplayMode(1600, 900));

        // Load the block texture atlas.
        try {
            TextureUtils.getTexture(ImageIO.read(new File("images/textures.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create camera. 70 fov.
        final Camera camera = new Camera(0, -32, 0, 70, 1000f, 0.4f);
        IjWindow.setCamera(camera);
        camera.setEntity(player);

        // Move the player and set their camera rotation.
        player.setY(-32);
        player.setX(40f);
        player.setZ(40f);
        player.setYaw(135f);

        // Give ijen3D the main render function
        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                try {
                    // Render the world.
                    for (Chunk chunk : getPlayer().getPlanet().getChunks()) {
                        chunk.render();
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }

                // Render the entities.
                for(Entity entity : SixEngineClient.entityGroup.getAllEntities()) {
                    entity.render();
                }
            }
        });

        // Thread dedicated just to updating the camera position. Could be merged with render thread?
        Thread camThread = new Thread(new Runnable() {
            @Override
            public void run() {
                float sensitivity = 0.3f;

                while (true) {
                    player.setPitch(player.getPitch() + Mouse.getDY() * -sensitivity);
                    player.setYaw(player.getYaw() + Mouse.getDX() * sensitivity);
                }
            }
        });
        camThread.setName("6Engine Camera Update Thread");
        camThread.start();

        // Thread that maintains client logic.
        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double shift = 0;
                long lastTime = System.currentTimeMillis();
                long lastTick = System.currentTimeMillis();

                while(true) {
                    long delta = System.currentTimeMillis() - lastTick;

                    // Perform logic on entities.
                    entityGroup.logic();

                    // Run player logic.
                    player.logic(delta);

                    System.out.println("fps: " + IjWindow.lastFrames);

                    // Block destruction test. Doesn't really work.
                    if(Mouse.isButtonDown(0)) {
                        if(System.currentTimeMillis() > lastTime + 150) {
                            Block cB = Block.AIR;

                            for (double i = 0; i < 40; i+=0.2) {
                                int x = (int) (player.getPX() + i * new Float(Math.cos(Math.toRadians(player.getYaw() + 90))));
                                int y = (int) (player.getPY() + i * new Float(Math.cos(Math.toRadians(player.getPitch() + 90))));
                                int z = (int) (player.getPZ() + i * new Float(Math.sin(Math.toRadians(player.getYaw() + 90))));

                                Block selectedBlock = getUniverse().getPlanetAt(0,0,0).getBlock(x, y, z);

                                if(selectedBlock != Block.AIR) {
                                    getUniverse().getPlanetAt(0,0,0).setBlock(x, y, z, Block.AIR);
                                    i = 31;
                                }
                            }

                            lastTime = System.currentTimeMillis();
                        }
                    }

                    // If right mouse button is down, place stone block at player's location and send a block placement
                    // packet to the server.
                    else if(Mouse.isButtonDown(1)) {
                        player.getPlanet().setBlock(player.getPX(), player.getPY(), player.getPZ(), Block.STONE);

                        try {
                            Client.dataOutputStream.writeByte(2);
                            Client.dataOutputStream.writeInt(Block.STONE.getID());
                            Client.dataOutputStream.writeInt(player.getPX());
                            Client.dataOutputStream.writeInt(player.getPY());
                            Client.dataOutputStream.writeInt(player.getPZ());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    lastTick = System.currentTimeMillis();

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        logicThread.setName("6Engine Logic Thread");
        logicThread.start();

        // Grab mouse in window
        Mouse.setGrabbed(true);

        // Set the sky color.
        IjWindow.setGLClearColor(0.45f,0.65f,1f);

        // Start rendering
        IjWindow.beginRendering();
    }

    public static Universe getUniverse() {
        return universe;
    }

    public static Player getPlayer() {
        return player;
    }



//                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "1", new String[]{"menger", "2", "3"},
//                                    new Double(player.getPX()).intValue(), player.getPY(), player.getPZ() - 20);
////
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            shift+= Math.PI / 2;
}
