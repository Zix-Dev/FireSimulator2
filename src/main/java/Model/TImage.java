package Model;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class TImage extends BufferedImage {

    //Attributes


    protected Configuration config;
    protected int[][] pixels;
    protected int width, height;
    protected Palette palette;


    //Constructor



    public TImage(int width, int height) {
        super(width, height+10, TYPE_INT_ARGB);
        this.width = width;
        this.height = height+10;
        this.pixels = new int[height+10][width];
        palette = new Palette(Color.BLACK, Color.WHITE);
    }



    //Getters & Setters


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }


    //Methods


    public void blur() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int count = 1;
                int total = pixels[x][y];
                if (x > 0) {total += pixels[x-1][y]; count++;}
                if (x < height-1) {total += pixels[x+1][y]; count++;}
                if (y > 0) {total += pixels[x][y-1]; count++;}
                if (y < width-1) {total += pixels[x][y+1]; count++;}

                pixels[x][y] = total/count;
            }
        }
    }

    public void moveUp(int speed) {
        int[][] aux = new int[speed][width];
        System.arraycopy(pixels, 0, aux, 0, speed);
        if (height - speed >= 0) System.arraycopy(pixels, speed, pixels, 0, height - speed);
        if (height - (height - speed) >= 0)
            System.arraycopy(aux, 0, pixels, height - speed, height - (height - speed));
    }

    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(
                new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    public void rotateClockWise(int times) {
        for (int i = 0; i++< times;) {
            int[][] pixelsRotated = new int[width][height];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixelsRotated[y][x] = pixels[x][y];
                }
            }
            pixels = pixelsRotated;
        }
    }

    public abstract void update();

    public void updateImage() {
        for (int y = 0; y < height-10; y++) {
            for (int x = 0; x < width; x++) {
                setRGB(x,y,palette.getTemperatureColorInt(pixels[y][x]));
            }
        }
    }

}
