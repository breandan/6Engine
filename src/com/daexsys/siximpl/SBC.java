package com.daexsys.siximpl;

import com.avaje.ebean.Expression;
import com.daexsys.depthz.TextureUtils;
import com.daexsys.ijen3D.Camera;
import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.ijen3D.entity.EntityGroup;
import com.daexsys.sixapi.SixCache;
import com.daexsys.siximpl.entity.BPlacer;
import com.daexsys.siximpl.entity.Player;
import com.daexsys.siximpl.net.client.Client;
import com.daexsys.siximpl.net.Server;
import com.daexsys.siximpl.world.Universe;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class SBC {
    public static Player player;
    private static Universe universe;
    public static EntityGroup entityGroup;
    public static Client client;

    public static void main(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        universe = new Universe();
        player = new Player(getUniverse().getPlanetAt(0, 0, 0), 0,0,0);

        entityGroup = new EntityGroup();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.startServer();
            }
        }).start();

        client = new Client();
        client.connect("127.0.0.1", 2171);
        init();
    }

    public static void init() {
        IjWindow.create("6Engine", new DisplayMode(1600, 900));

        try {
            TextureUtils.getTexture(ImageIO.read(new File("images/textures.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Camera camera = new Camera(0, -32, 0, 70, 1000f, 0.4f);
        IjWindow.setCamera(camera);
        camera.setEntity(player);
        player.setY(-32);
        player.setX(115.2f);
        player.setZ(115.2f);
        player.setYaw(135f);

        IjWindow.setGLClearColor(135, 250, 250);

        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                try {
                    for (Chunk chunk : getPlayer().getPlanet().getChunks()) {
                        chunk.render();    }
                    } catch (Exception e) {
                }

                    for(Entity entity : SBC.entityGroup.getAllEntities()) {
                        entity.render();
                    }

            }
        });

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
        camThread.start();

        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double shift = 0;
                long lastTime = System.currentTimeMillis();
                long lastTick = System.currentTimeMillis();

                while(true) {
                    long delta = System.currentTimeMillis() - lastTick;
                    entityGroup.logic();

                    // Run player logic.
                    player.logic(delta);

                    System.out.println("fps: " + IjWindow.lastFrames);

                    if(Mouse.isButtonDown(0)) {

                        if(System.currentTimeMillis() > lastTime + 150) {
                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "1", new String[]{"menger", "2", "4"},
                                    new Double(player.getPX()).intValue(), player.getPY(), player.getPZ() - 20);


                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            shift+= Math.PI / 2;
                            lastTime = System.currentTimeMillis();
                        }
                    } else if(Mouse.isButtonDown(1)) {
                            player.getPlanet().setBlock(player.getPX(), player.getPY(), player.getPZ(), Block.STONE);
                    }

//                        player.setY(-32);
//                        player.setX(115.2f);
//                        player.setZ(115.2f);

                    lastTick = System.currentTimeMillis();

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        logicThread.setName("SBG Logic Thread");
        logicThread.start();

        Mouse.setGrabbed(true);
        IjWindow.setGLClearColor(0.2f,0.4f,0.8f);

        IjWindow.beginRendering();
    }

    public static Universe getUniverse() {
        return universe;
    }

    public static Player getPlayer() {
        return player;
    }
}
