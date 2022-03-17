package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Palette {


    //Attributes


    private final int[] colors;
    private final Color[] baseColors;


    //Constructors

    public Palette(Color... colorList) {
        colors = new int[256];
        baseColors = colorList;
        generatePaletteGradient();
    }


    //Methods


    public int[] getARGB(int color) {

        int a = ((color >> 24) & 0xFF);
        int r = ((color >> 16) & 0xFF);
        int g = ((color >> 8) & 0xFF);
        int b = ((color) & 0xFF);

        return new int[]{a, r, g, b};
    }

    public Color[] getBaseColors() {
        return baseColors;
    }


    public Color getTemperatureColor(int t) {
        int[] argb = getARGB(getTemperatureColorInt(t));
        return new Color(argb[1], argb[2], argb[3], argb[0]);
    }
    public int getColorInt(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int getColorInt(Color c) {
        return (c.getAlpha() << 24) | (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    public int getTemperatureColorInt(int t) {
        if (t > 255) {
            return colors[255];
        } else if (t < 0) {
            return colors[0];
        }
        return colors[t];
    }

    public void generatePaletteGradient() {
        int[][] baseColorsArgb = new int[baseColors.length][4];
        for (int i = 0; i < baseColors.length; i++) {
            Color c = baseColors[i];
            baseColorsArgb[i] = new int[]{c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()};
        }

        float distanceBetweenColors = 255f / (float) (baseColors.length);
        int[] sections = new int[baseColors.length+1];
        for (int i = 1; i < baseColors.length; i++) {
            sections[i] = Math.round(distanceBetweenColors*(i+1));
        }

        for (int i = 0; i < baseColors.length; i++) {
            int sectionSize = (i > 0)?(sections[i]-sections[i-1]):sections[i];
            int[] gradientSection = new int[0];
            if (i == 0) {
                gradientSection(sectionSize, baseColorsArgb[0], baseColorsArgb[1]);
            } else {
                gradientSection = gradientSection(sectionSize, baseColorsArgb[i - 1], baseColorsArgb[i]);
            }
            for (int j = (i > 0)?sections[i-1]:0, k = 0; j < sections[i]; j++, k++) {
                colors[j] = gradientSection[k];
            }
        }

    }

    public int[] gradientSection(int sectionSize, int[] color1, int[] color2) {
        float multiplier = 0;
        float multiplierIncrement = 1f / (sectionSize);
        int[] section = new int[sectionSize + 1];
        for (int i = 0; i < sectionSize; i++, multiplier += multiplierIncrement) {

            int[] argb = new int[]{
                    (int) ((color1[0] * (1 - multiplier)) + (color2[0] * multiplier)),
                    (int) ((color1[1] * (1 - multiplier)) + (color2[1] * multiplier)),
                    (int) ((color1[2] * (1 - multiplier)) + (color2[2] * multiplier)),
                    (int) ((color1[3] * (1 - multiplier)) + (color2[3] * multiplier))
            };
            section[i] = getColorInt(argb[0], argb[1], argb[2], argb[3]);
        }
        return section;
    }

    public ImageIcon toIcon() {
        return toIcon("");
    }

    public ImageIcon toIcon(String s) {
        BufferedImage icon = new BufferedImage(300,30, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = icon.createGraphics();

        g.setColor(new Color(colors[0]));
        g.fillRect(0,0,300,30);
        int x = 300-255;
        for (int i : colors) {
            g.setColor(new Color(i));
            g.fillRect(x,0,1, 30);
            x++;
        }
        int[] argb = getARGB(colors[0]);
        if (argb[1]+argb[2]+argb[3] < 300) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString(s,5,20);
        icon.flush();
        return new ImageIcon(icon);
    }

}
