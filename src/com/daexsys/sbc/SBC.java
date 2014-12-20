package com.daexsys.sbc;

import com.daexsys.depthz.TextureUtils;
import com.daexsys.ijen3D.Camera;
import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.sbc.entity.Player;
import com.daexsys.sbc.world.Universe;
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

        final Camera camera = new Camera(0, 0, 0, 90, 250f, 0.8f);
        IjWindow.setCamera(camera);

        final Chunk chunk = new Chunk(0,0,0);
        final Chunk chunk2 = new Chunk(1,0,1);
        final Chunk chunk3 = new Chunk(-1,0,-1);

        for (int i = 0; i < 16; i++) {
            chunk.setXYArea(Block.DIRT, i);
            chunk2.setXYArea(Block.DIRT, i);
            chunk3.setXYArea(Block.DIRT, i);
        }

        chunk.rebuild();
        chunk2.rebuild();
        chunk3.rebuild();

        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                chunk.render();
                chunk2.render();
                chunk3.render();

                System.out.println(camera.getYaw());
                System.out.println(camera.getPitch());
            }
        });

        Mouse.setGrabbed(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    camera.setPitch(camera.getPitch() + Mouse.getDY() * -0.3f);
                    camera.setYaw(camera.getYaw() + Mouse.getDX() * 0.3f);

                    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                        camera.setX(camera.getX() + 0.05f);
                    } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                        camera.setX(camera.getX() - 0.05f);
                    }

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        IjWindow.setGLClearColor(0.5f,0.5f,0.5f);
        IjWindow.beginRendering();
    }

    public static Universe getUniverse() {
        return universe;
    }
}
