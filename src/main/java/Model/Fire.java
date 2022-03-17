package Model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Fire extends TImage implements Runnable {


    //Attributes


    private final Igniter igniter;
    private final CoolingMap coolingMap;
    private int[][] igniterPixels;


    //Constructor


    public Fire(int width, int height) {
        super(width, height);
        config = new Configuration();
        coolingMap = new CoolingMap(width, height, config);
        igniter = new Igniter(width, config);
        igniterPixels = new int[this.height][this.width];
    }


    //Overridden methods


    @Override
    public void run() {
        while (true) {
            if (!config.paused) {
                update();
            }
            try {
                sleep(config.getUpdateDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //Methods


    public void blurWithKernel() {
        int[][] p = new int[height][width];
        boolean[][] kernel = config.getKernel();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int total = 0;
                int count = 0;

                if (kernel[0][0] && x > 1 && y > 1) {total += pixels[x-2][y-2];count++;}
                if (kernel[0][1] && x > 1 && y > 0) {total += pixels[x-2][y-1];count++;}
                if (kernel[0][2] && x > 1) {total += pixels[x-2][y];count++;}
                if (kernel[0][3] && x > 1 && y < width-1) {total += pixels[x-2][y+1];count++;}
                if (kernel[0][4] && x > 1 && y < width-2) {total += pixels[x-2][y+2];count++;}

                if (kernel[1][0] && x > 0 && y > 1) {total += pixels[x-1][y-2];count++;}
                if (kernel[1][1] && x > 0 && y > 0) {total += pixels[x-1][y-1];count++;}
                if (kernel[1][2] && x > 0) {total += pixels[x-1][y];count++;}
                if (kernel[1][3] && x > 0 && y < width-1) {total += pixels[x-1][y+1];count++;}
                if (kernel[1][4] && x > 0 && y < width-2) {total += pixels[x-1][y+2];count++;}

                if (kernel[2][0] && y > 1) {total += pixels[x][y-2];count++;}
                if (kernel[2][1] && y > 0) {total += pixels[x][y-1];count++;}
                if (kernel[2][2]) {total += pixels[x][y];count++;}
                if (kernel[2][3] && y < width-1) {total += pixels[x][y+1];count++;}
                if (kernel[2][4] && y < width-2) {total += pixels[x][y+2];count++;}

                if (kernel[3][0] && x < height-1 && y > 1) {total += pixels[x+1][y-2];count++;}
                if (kernel[3][1] && x < height-1 && y > 0) {total += pixels[x+1][y-1];count++;}
                if (kernel[3][2] && x < height-1) {total += pixels[x+1][y];count++;}
                if (kernel[3][3] && x < height-1 && y < width-1) {total += pixels[x+1][y+1];count++;}
                if (kernel[3][4] && x < height-1 && y < width-2) {total += pixels[x+1][y+2];count++;}

                if (kernel[4][0] && x < height-2 && y > 1) {total += pixels[x+2][y-2];count++;}
                if (kernel[4][1] && x < height-2 && y > 0) {total += pixels[x+2][y-1];count++;}
                if (kernel[4][2] && x < height-2) {total += pixels[x+2][y];count++;}
                if (kernel[4][3] && x < height-2 && y < width-1) {total += pixels[x+2][y+1];count++;}
                if (kernel[4][4] && x < height-2 && y < width-2) {total += pixels[x+2][y+2];count++;}

                if (count == 0) {
                    p[x][y] = 0;
                    continue;
                }
                p[x][y] = total/count;
            }
        }
        this.pixels = p;
    }

    public void changeCoolingType() throws IOException {

        int coolingType = config.getCoolingType();

        coolingType = (coolingType == 2) ? 0 : coolingType + 1;

        coolingMap.importCoolingMap("flat.png");
        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coolingMap.importCoolingMap("flat.png");

        config.setCoolingType(coolingType);

        coolingMap.updateCoolingType();

    }

    public void convection(int speed) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {

                if (x < height - speed) {
                    pixels[x][y] = pixels[x + speed][y];
                }

            }
        }
    }

    public void cool(float power) {

        if (config.isUsingCoolingMap()) {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {

                    pixels[x][y] -= coolingMap.pixels[x][y] * (power / 30f);
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }

        } else {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    pixels[x][y] -= 3f/4f*power;
                    if (pixels[x][y] < 0) {
                        pixels[x][y] = 0;
                    }

                }
            }
        }
    }

    public Configuration getConfiguration() {
        return config;
    }

    public CoolingMap getCoolingMap() {
        return coolingMap;
    }

    public void ignite(int speed, float density) {
        switch (config.getIgniterType()) {
            case Configuration.LINE:
                igniteLine(density,speed);
                break;
            case Configuration.RANDOM:
                igniteRandom(speed, density);
                break;
            case Configuration.MEMORY:
                igniteMemory(speed);
                break;
            case Configuration.CONVOLUTION:
                igniteCustomPixels(density);
                break;
        }
    }

    private void igniteLine(float density, int speed) {
        for (int x = height - speed; x < height; x++) {
            for (int y = 0; y < width; y++) {

                pixels[x][y] = (int) (density * 2.55f);

            }
        }
    }

    public void igniteRandom(int lines, float density) {
        for (int x = height - lines; x < height; x++) {

            int ignitions = (int) (width * (density / 100f));

            for (Random r = new Random(); ignitions > 0; ignitions--) {
                int ry = r.nextInt(width);
                pixels[x][ry] = (int) ((r.nextFloat() * (105f - density)) + density + 150f);
            }
        }
    }

    public void igniteMemory(int speed) {
        igniter.update();
        for (int x = height - speed; x < height; x++) {
            pixels[x] = igniter.getPixels();
        }
    }

    public void igniteCustomPixels(float density) {
        Random r = new Random();
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                pixels[x][y] = (igniterPixels[x][y]>1)?r.nextInt(pixels[x][y]+(int) ((igniterPixels[x][y]*density)+1)):pixels[x][y];
                if (pixels[x][y] > 255) pixels[x][y] = 255;
            }
        }
    }

    public void update() {
        coolingMap.update();
        if (config.getIgniterType() == 2) {igniter.update();}
        ignite(config.getSpeed(), config.getIgnitionDensity());
        if (config.convectionEnabled) {convection(config.getSpeed());}
        blurWithKernel();
        cool(config.getCoolingPower() * (0.5f + (new Random().nextFloat())));
        updateImage();
    }

    public void setIgniterPixels(int[][] igniterPixels) {
        if (igniterPixels.length < pixels.length || igniterPixels[0].length < pixels[0].length ) {
            throw new IllegalArgumentException();
        }
        this.igniterPixels = igniterPixels;
    }
}
