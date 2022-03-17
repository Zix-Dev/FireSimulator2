package Model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Convolution extends BufferedImage {


    //Attributes


    private BufferedImage originalImage;
    private short[][][] channels; //RGBA
    private float[][] kernel;


    //Constructor


    public Convolution(BufferedImage originalImage) {
        super(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        this.originalImage = originalImage;
        this.channels = new short[4][originalImage.getWidth()][originalImage.getHeight()];
        splitChannels();
        this.kernel = new float[3][3];
        kernel[kernelCenter()][kernelCenter()] = 1f;
        convolve();
    }


    //Methods


    private void splitChannels() {
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int pixel = originalImage.getRGB(x,y);
                short a = (short) ((pixel >> 24) & 0xFF);
                short r = (short) ((pixel >> 16) & 0xFF);
                short g = (short) ((pixel >> 8) & 0xFF);
                short b = (short) ((pixel) & 0xFF);

                channels[0][x][y] = r;
                channels[1][x][y] = g;
                channels[2][x][y] = b;
                channels[3][x][y] = a;
            }
        }
    }

    private void convolve() {

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                convolvePixel(x,y);
            }
        }

    }

    private void convolvePixel(int x, int y) {

        short[] rgba = new short[4];

        for (int kx = 0; kx < kernel.length; kx++) {
            for (int ky = 0; ky < kernel.length; ky++) {

                int xOffset = kx - kernelCenter();
                int yOffset = ky - kernelCenter();

                for (int i = 0; i < 4; i++) {
                    if (x + xOffset < 0 || x + xOffset >= getWidth()) {break;}
                    if (y + yOffset < 0 || y + yOffset >= getHeight()) {break;}
                    rgba[i] += channels[i][x+xOffset][y+yOffset] * kernel[kx][ky];
                }


            }
        }

        for (int i = 0; i < 4; i++) {
            rgba[i] = (rgba[i] > 255)?rgba[i] = 255:rgba[i];
            rgba[i] = (rgba[i] < 0)?rgba[i] = 0:rgba[i];
        }

        setRGB(x,y,(rgba[3] << 24) | (rgba[0] << 16) | (rgba[1] << 8) | rgba[2]);

    }

    private int kernelCenter() {
        return kernel.length/2;
    }

    public float[][] getKernel() {
        return kernel;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setKernel(float[][] kernel) {
        if (kernel.length % 2 == 0) {
            throw new IllegalArgumentException("Kernel size must not be pair");
        }
        this.kernel = kernel;
        convolve();
    }

    public void setThreshold(int threshold) {
        convolve();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {

                int c = getRGB(x,y);

                short r = (short) ((c >> 16) & 0xFF);
                short g = (short) ((c >> 8) & 0xFF);
                short b = (short) ((c) & 0xFF);

                int brightness = (r+g+b)/3;

                if (brightness < threshold) setRGB(x,y,0);


            }
        }
    }

    public int[][] getBrightnessMap() {
        int[][] map = new int[getHeight()][getWidth()];

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {

                int c = getRGB(x,y);

                short r = (short) ((c >> 16) & 0xFF);
                short g = (short) ((c >> 8) & 0xFF);
                short b = (short) ((c) & 0xFF);

                int brightness = (r+g+b)/3;

                map[y][x] = brightness;


            }
        }
        return map;
    }

    public void setOriginalImage(BufferedImage img) {
        this.originalImage = img;
    }

}
