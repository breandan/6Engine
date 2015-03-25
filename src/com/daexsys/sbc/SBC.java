package com.daexsys.sbc;

import com.daexsys.depthz.TextureUtils;
import com.daexsys.ijen3D.Camera;
import com.daexsys.ijen3D.Coordinate;
import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.ijen3D.entity.EntityGroup;
import com.daexsys.sbc.entity.BPlacer;
import com.daexsys.sbc.entity.Player;
import com.daexsys.sbc.net.client.Client;
import com.daexsys.sbc.net.Server;
import com.daexsys.sbc.world.Universe;
import com.daexsys.sbc.world.block.Air;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

import com.daexsys.sbc.world.planet.generator.PlanetGenerator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class SBC {
    public static Player player;
    private static Universe universe;
    public static EntityGroup entityGroup;

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

        Client client = new Client();
        client.connect("127.0.0.1", 2171);
//        SBGClient.connect();
        init();
    }

    public static void init() {
        IjWindow.create("Blocks in Space", new DisplayMode(1024, 576));

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

        final Camera camera = new Camera(0, -32, 0, 90, 250f, 0.8f);
        IjWindow.setCamera(camera);
        camera.setEntity(player);
        player.setY(-32);
//
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 10; j++) {
//                for (int k = 0; k < 6; k++) {
//                    new PlanetGenerator(getPlayer().getPlanet()).generate(i,j,k);
//                }
//            }
//        }

        SBC.getPlayer().getPlanet().rebuild();

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
                while(true) {
                    entityGroup.logic();
                    // Mouse Rotation
                    player.setPitch(player.getPitch() + Mouse.getDY() * -0.3f);
                    player.setYaw(player.getYaw() + Mouse.getDX() * 0.3f);

                    // Run player logic.
                    player.logic();

                    if(Mouse.isButtonDown(0)) {
                        System.out.println(player.getPX() + " " + player.getPY() + " " + player.getPZ());

                        BlockCoord coordinate = player.getNearestBlock();
                        System.out.println(coordinate);
                        if(coordinate != null) {
                            player.getPlanet().setBlock(coordinate.x, coordinate.y, coordinate.z, Block.DIRT);
                        }

                        entityGroup.addEntity(new BPlacer(getPlayer().getX(), getPlayer().getY() * -1, getPlayer().getZ(),
                                -getPlayer().getPitch(), getPlayer().getYaw()));
//                        player.getPlanet().setBlock(player.getPX(), player.getPY(), player.getPZ(), Block.DIRT);
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
