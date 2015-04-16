package com.daexsys.siximpl;

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

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
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
//        SBGClient.connect();
        init();
    }

    public static void init() {
        IjWindow.create("6Engine", new DisplayMode(1600, 900));

        try {
            TextureUtils.getTexture(ImageIO.read(new File("images/dirt.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TextureUtils.getTexture(ImageIO.read(new File("images/grass.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TextureUtils.getTexture(ImageIO.read(new File("images/stone.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Camera camera = new Camera(0, -32, 0, 90, 1000f, 0.4f);
        IjWindow.setCamera(camera);
        camera.setEntity(player);
        player.setY(-32);

        IjWindow.setGLClearColor(135, 250, 250);

        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                try {
                    for (Chunk chunk : getPlayer().getPlanet().getChunks()) {
                        chunk.render();
                    }

                    for(Entity entity : SBC.entityGroup.getAllEntities()) {
                        entity.render();
                    }
                } catch (Exception e) {
                }
            }
        });

        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double shift = 0;
                long lastTime = System.currentTimeMillis();
                SixCache oldBlocks = new SixCache();

                while(true) {
                    entityGroup.logic();
                    // Mouse Rotation
                    player.setPitch(player.getPitch() + Mouse.getDY() * -0.3f);
                    player.setYaw(player.getYaw() + Mouse.getDX() * 0.3f);

                    // Run player logic.
                    player.logic(3);

                    if(Mouse.isButtonDown(0)) {
                        player.logic();
//
//                        IjWindow.addRenderer(new Renderer() {
//                            @Override
//                            public void render() {
//                                universe.getPlanetAt(0,0,0).rebuildRenderGeometry();
//                            }
//                        });
//                        System.out.println(player.getPX() + " " + player.getPY() + " " + player.getPZ());
//
//                        BlockCoord coordinate = player.getNearestBlock();
//                        System.out.println(coordinate);
//                        if(coordinate != null) {







                        if(System.currentTimeMillis() > lastTime + 150) {
                            int length = 200;
                            universe.getPlanetAt(0, 0, 0).clearTempBlocks();
                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "1",
//                            System.out.println("User at: " + player.getPX() + " " + player.getPY() + " " + player.getPZ());
                                    new String[]{"circle", "1", "10", "1"}, player.getPX(), player.getPY(), player.getPZ());

                            universe.getPlanetAt(0,0,0).clearTempBlocks();
                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "1",
                                    new String[]{"cos", "1", length + "", "10", "10", shift + ""}, new Double(player.getPX()).intValue(), player.getPY(), player.getPZ() - 20);
                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "1",
                                    new String[]{"tan", "1", length + "", "10", "10", shift + ""}, new Double(player.getPX()).intValue(), player.getPY(), player.getPZ() - 20);
                            ExpressionParser.parseAndPrint(universe.getPlanetAt(0, 0, 0), "5",
                                    new String[]{"sin", "1", length + "", "10", "10", shift + ""}, new Double(player.getPX()).intValue(), player.getPY(), player.getPZ() - 20);
                            shift+= Math.PI / 2;
                            lastTime = System.currentTimeMillis();
                        }


//                        player.getPlanet().setBlock(player.getPX(), player.getPY(), player.getPZ(), Block.DIRT);
                    } else if(Mouse.isButtonDown(1)) {
                            player.getPlanet().setBlockNoRebuild(player.getPX(), player.getPY(), player.getPZ(), Block.GRASS);

                    }

                    // Pause thread for 25 milliseconds.
                    try {
                        Thread.sleep(25);
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
