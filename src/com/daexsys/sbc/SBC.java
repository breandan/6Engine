package com.daexsys.sbc;

import com.daexsys.depthz.TextureUtils;
import com.daexsys.ijen3D.Camera;
import com.daexsys.ijen3D.Coordinate;
import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.sbc.entity.Player;
import com.daexsys.sbc.net.SBGClient;
import com.daexsys.sbc.net.SBGServer;
import com.daexsys.sbc.world.Universe;
import com.daexsys.sbc.world.block.Air;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class SBC {
    public static Player player;
    private static Universe universe;

    public static void main(String[] args) {
        SBGServer.startServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        SBGClient.connect();

        universe = new Universe();
        player = new Player(getUniverse().getPlanetAt(0, 0, 0), 0,0,0);
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

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 6; k++) {
                    getPlayer().getPlanet().addChunk(new Chunk(i,j,k));
                }
            }
        }

        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                try {
                    for (Chunk chunk : getUniverse().getStarterPlanet().getChunks()) {
                        chunk.render();
                    }
                } catch (Exception e) {

                }
            }
        });

        Mouse.setGrabbed(true);

        Thread logicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    // Mouse Rotation
                    player.setPitch(player.getPitch() + Mouse.getDY() * -0.3f);
                    player.setYaw(player.getYaw() + Mouse.getDX() * 0.3f);

                    // Run player logic.
                    player.logic();

                    if(Mouse.isButtonDown(0)) {
//                        for (int amplitude = 0; amplitude < 10; amplitude+=1) {
//                            Coordinate coordinate = player.getCoordinateLookingAt(amplitude);
//
//                            Block atLoc = player.getPlanet().getBlock(
//                                    (int) coordinate.getX() / 2,
//                                    (int) coordinate.getY() * -1 / 2,
//                                    (int) coordinate.getZ() / 2);
//
//                            System.out.println("Location: "+player.getX() + " "+player.getY() + " "+player.getZ());
//                            System.out.println("ampl: "+amplitude+ " " +coordinate.getX() + " "+coordinate.getY() + " "+coordinate.getZ());
//
//                            if(!(atLoc instanceof Air)) {
//                                player.getPlanet().setBlock((int) coordinate.getX(), (int) coordinate.getY(), (int) coordinate.getZ(), Block.DIRT);
//                                amplitude = 31;
//                            }
//                        }
                        System.out.println((int) player.getX() / 2 + " "+(int) player.getY() * - 1/ 2+ " "+ (int) player.getZ()/ 2);

                        player.getPlanet().setBlock((int) player.getX() / 2, (int) player.getY() * -1 / 2, (int) player.getZ()/ 2, Block.DIRT);
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
