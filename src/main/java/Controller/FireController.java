package Controller;

import Model.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FireController {


    //Attributes


    private final Fire fire;
    private final Configuration config;
    private final CoolingMap coolingMap;
    private final PaletteSaver paletteSaver;

    private Convolution convolution;

    private final Color backgroundColor = new Color(50,50,60);
    private final Color foregroundColor = new Color(230,230,230);
    private final Color buttonColor = new Color (239, 126, 40);
    private final Color selectedButtonColor = new Color (173, 44, 12);


    //Constructor


    public FireController(Fire fire) {
        this.fire = fire;
        this.config = fire.getConfiguration();
        this.coolingMap = fire.getCoolingMap();
        this.paletteSaver = new PaletteSaver();
        try {
            BufferedImage img = ImageIO.read(new File("src\\main\\resources\\Images\\default.jpg"));
            this.convolution = new Convolution(img);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    //Getters


    public Fire getFire() {
        return fire;
    }

    public Convolution getConvolution() {
        return convolution;
    }

    public Configuration getConfig() {
        return config;
    }

    public CoolingMap getCoolingMap() {
        return coolingMap;
    }

    public PaletteSaver getPaletteSaver() {
        return paletteSaver;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public Color getSelectedButtonColor() {
        return selectedButtonColor;
    }


    //Methods


    public void setConvolutionKernel(float[][] kernel) {
        convolution.setKernel(kernel);
        fire.setIgniterPixels(convolution.getBrightnessMap());
    }

    public void setConvolutionThreshold(int threshold) {
        convolution.setThreshold(threshold);
        fire.setIgniterPixels(convolution.getBrightnessMap());
    }

    public void convectionSetEnabled(boolean b) {
        config.convectionEnabled = b;
    }

    public void export(String path, int duration, JProgressBar bar) throws IOException, InterruptedException {

        boolean wasPaused = config.paused;

        config.paused = true;

        bar.setMinimum(0);
        bar.setMaximum(duration*60);

        ImageOutputStream output = new FileImageOutputStream(new File(path));

        GifSequenceWriter writer = new GifSequenceWriter(output, fire.getType(), (int) config.getUpdateDelay(), true);
        for (int i = 0; i++<duration*60;) {
            writer.writeToSequence(fire);
            bar.setValue(i);
            if (wasPaused) {continue;}
            fire.update();
        }

        writer.close();
        output.close();

        config.paused = wasPaused;
    }

    public void savePalette(Palette palette, String name) {
        paletteSaver.add(name,palette);
    }

    public void setCoolingType(int type) {
        boolean p = config.paused;
        config.paused = true;
        config.setCoolingType(type);
        try {
            coolingMap.updateCoolingType();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        config.paused = p;
    }

    public void setCoolingPower(int power) {
        config.setCoolingPower(power);
    }

    public void setFireKernel(boolean[][] kernel) {
        config.setKernel(kernel);
    }

    public void setFirePalette(Palette palette) {
        fire.setPalette(palette);
    }

    public void setFirePalette(String paletteName) {
        fire.setPalette(paletteSaver.get(paletteName));
    }

    public void setIgniterType(int type) {
        config.setIgniterType(type);
    }

    public void setUpdateDelay(long millis) {
        config.setUpdateDelay(millis);
    }

    public void removePalette(String paletteName) {
        paletteSaver.remove(paletteName);
    }

    public void setIgniterIntensity(int intensity) {
        config.setIgnitionDensity(intensity);
        config.setIgniterMaxSize(intensity*3);
    }

    public void setNextImportedCoolingMap() {
        config.nextCoolingPath();
        coolingMap.updateImportedCoolingMap();
    }
}
