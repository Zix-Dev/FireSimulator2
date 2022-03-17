package Model;

import java.util.Random;
import java.util.Stack;

public class Igniter {


    // Attributes


    private int[] pixels;
    private final Stack<float[]> ignitions;
    private final Configuration config;


    // Constructor


    public Igniter(int fireWidth, Configuration config) {
        this.pixels = new int[fireWidth];
        this.ignitions = new Stack<>();
        this.config = config;
        addSparks(config.getIgniterCount());
        centerIgnitions();
    }


    // Methods


    public synchronized void addSparks(int number) {
        for (int i = 0; i < number; i++) {
            ignitions.push(new float[]{new Random().nextInt(pixels.length),150f});
        }
    }

    public synchronized void attractToCenter(float speed) {
        float center = (pixels.length/2f);
        for (float[] ignition : ignitions) {
            float relativeDistanceFromCenter = (((ignition[0] - center) / center));
            ignition[0] -= relativeDistanceFromCenter * ((new Random().nextFloat()) * (speed / 90f));
            if (ignition[1] < 0) {
                ignition[1] = 0;
            }
        }
    }

    public synchronized void centerIgnitions() {
        float positionIncrement = ((float)pixels.length)/((float) ignitions.size());
        float position = positionIncrement;
        for (float[] ignition : ignitions) {
            ignition[0] = position;
            ignition[1] = (new Random().nextFloat()) * 255;
            position += positionIncrement;
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public synchronized void move(float speed) {
        int relativeSpeed = (int) ((pixels.length/2)*(speed/1500));
        Random r = new Random();
        for (float[] ignition : ignitions) {
            int min = -relativeSpeed;
            ignition[0] += r.nextInt((relativeSpeed - min) + 1) + min;
            if (ignition[0] < 0) {
                ignition[0] = 0;
            }
            if (ignition[0] >= pixels.length) {
                ignition[0] = pixels.length - 1;
            }
        }
    }

    public synchronized void removeSparks(int number) {
        for (int i = 0;  i++<number && ignitions.size() > 0;) {
            ignitions.pop();
        }
    }

    public synchronized void spread(float maxSize) {
        pixels = new int[pixels.length];
        Random r = new Random();

        for (float[] ignition : ignitions) {

            float size = maxSize * (ignition[1] / 255f);
            int ignitionLength = (int) ((pixels.length / 2f) * (size / 100f));
            float multiplierDecrement = 1f / ignitionLength;

            ignition[1] += r.nextInt(41) - 22;
            if (ignition[1] < 0) {
                ignition[1] = 0;
            }
            if (ignition[1] > 255) {
                ignition[1] = 255;
            }

            float multiplier = 1 - multiplierDecrement;
            pixels[(int) ignition[0]] = (int) ignition[1];

            for (int j = 0; j < ignitionLength; j++) {
                if (ignition[0] - j > 0) {
                    pixels[(int) (ignition[0] - j)] += (int) (ignition[1] * multiplier * (r.nextFloat() + 0.5f));
                    if (pixels[(int) (ignition[0] - j)] > 255) {
                        pixels[(int) (ignition[0] - j)] = 255 - r.nextInt(200);
                    }
                    if (pixels[(int) (ignition[0] - j)] < 0) {
                        pixels[(int) (ignition[0] - j)] = 0;
                    }
                }
                if (ignition[0] + j < pixels.length) {
                    pixels[(int) (ignition[0] + j)] += (int) (ignition[1] * multiplier * (r.nextFloat() + 0.5f));
                    if (pixels[(int) (ignition[0] + j)] > 255) {
                        pixels[(int) (ignition[0] + j)] = 255 - r.nextInt(200);
                    }
                    if (pixels[(int) (ignition[0] + j)] < 0) {
                        pixels[(int) (ignition[0] + j)] = 0;
                    }
                }
                multiplier -= multiplierDecrement;
            }

        }
    }

    public void update() {
        move(config.getIgniterSpeed());
        attractToCenter(config.getIgniterSpeed());
        spread(config.getIgniterMaxSize());
    }

}
