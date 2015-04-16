package com.daexsys.siximpl;

import com.daexsys.sixapi.SixCache;
import com.daexsys.sixapi.SixWorld;

public class ExpressionParser {
    public static SixCache parseAndPrint(SixWorld sixWorld, String block, String[] arguments, int x, int y, int z) {
        SixCache sixCache = new SixCache();

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
            double shift = Double.parseDouble(arguments[5]);

            try {
                for (double i = -length; i < length; i+=.01) {
                    double angleInRadians = Math.toRadians((i + shift) * compression);

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
            double shift = Double.parseDouble(arguments[5]);

            try {
                for (double i = -length; i < length; i+=.001) {
                    double angleInRadians = Math.toRadians((i + shift)  * compression);

                    sixCache.alterBlockAt(block,
                            x + new Double(i).intValue(),
                            y + new Double(Math.tan(angleInRadians) * amplitude).intValue(),
                            z);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        sixCache.apply(sixWorld);
        return sixCache;
    }
}
