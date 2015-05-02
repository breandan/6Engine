package com.daexsys.siximpl;

import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.sixapi.SixCache;
import com.daexsys.sixapi.SixSet;
import com.daexsys.sixapi.SixWorld;

public class ExpressionParser {
    /**
     * Plots an expression into the block world.
     * @param sixWorld the world
     * @param block the block type
     * @param arguments the arguments
     * @param x the X coordinate of the origin of the plot
     * @param y the Y coordinate of the origin of the plot
     * @param z the Z coordinate of the origin of the plot
     * @return a cache of the result
     */
    public static SixCache parseAndPrint(final SixWorld sixWorld, final String block, final String[] arguments, final int x, final int y, final int z) {
        SixCache sixCache = new SixSet();

        if(arguments[0].equals("circle")) {
            double radius = Double.parseDouble(arguments[2]);
            double precision = Double.parseDouble(arguments[3]);

            for (double i = 0; i < 360; i+=precision) {
                double angleInRadians = Math.toRadians(i);

                sixCache.alterBlockAt(block,
                        new Double(x + Math.cos(angleInRadians) * radius).intValue(),
                        new Double(y + Math.sin(angleInRadians) * radius).intValue(),
                        z);
            }
        }

        else if(arguments[0].equalsIgnoreCase("cos")) {
            double length = Double.parseDouble(arguments[2]);
            double compression = Double.parseDouble(arguments[3]);
            double amplitude = Double.parseDouble(arguments[4]);
            double shift = Double.parseDouble(arguments[5]);

            try {
                for (double i = -length; i < length; i += 0.01) {
                    double angleInRadians = Math.toRadians((i + shift) * compression);

                    sixCache.alterBlockAt(block,
                            x + new Double(i).intValue(),
                            y + new Double(Math.cos(angleInRadians) * amplitude).intValue(),
                            z);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(arguments[0].equalsIgnoreCase("sin")) {
            double length = Double.parseDouble(arguments[2]);
            double compression = Double.parseDouble(arguments[3]);
            double amplitude = Double.parseDouble(arguments[4]);

            try {
                for (double i = -length; i < length; i+=.01) {
                    double angleInRadians = Math.toRadians(i * compression);

                    sixCache.alterBlockAt(block,
                            x + new Double(i).intValue(),
                            y + new Double(Math.sin(angleInRadians) * amplitude).intValue(),
                            z);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(arguments[0].equalsIgnoreCase("tan")) {
            double length = Double.parseDouble(arguments[2]);
            double compression = Double.parseDouble(arguments[3]);
            double amplitude = Double.parseDouble(arguments[4]);

            try {
                for (double i = -length; i < length; i+=.001) {
                    double angleInRadians = Math.toRadians(i * compression);

                    sixCache.alterBlockAt(block,
                            x + new Double(i).intValue(),
                            y + new Double(Math.tan(angleInRadians) * amplitude).intValue(),
                            z);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else if(arguments[0].equalsIgnoreCase("menger")) {
            final int level = Integer.parseInt(arguments[2]);

            Thread mengerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MengerGenerator mengerGenerator = new MengerGenerator(block, level, x, y, z);
                    System.out.println("Placing in world...");
                    mengerGenerator.getSixCache().apply(sixWorld, x, y, z);
                    System.out.println("Menger generated");

//                    System.exit(0);
                    IjWindow.addProcess(new Renderer() {
                        @Override
                        public void render() {
                            SixEngineClient.getUniverse().getPlanetAt(0, 0, 0).rebuildRenderGeometry();
                            IjWindow.removeProcess(this);
                        }
                    });
                }
            });

            mengerThread.setName("6Graph Menger Generation Thread");
            mengerThread.start();
        }
//
//        else if(arguments[0].equalsIgnoreCase("tree")) {
//            TreeGenerator treeGenerator = new TreeGenerator(block, y, x, y, z);
//            treeGenerator.getSixCache().apply(sixWorld);
//            System.out.println("tree generated");
//        }

        sixCache.apply(sixWorld);
        return sixCache;
    }
}
