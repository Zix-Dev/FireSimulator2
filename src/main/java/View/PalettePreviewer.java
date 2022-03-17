package View;

import Model.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Thread.sleep;

public class PalettePreviewer extends Canvas implements Runnable {

    private Palette palette;
    private int step = 254;
    private final boolean vertical;

    public PalettePreviewer(Palette palette, boolean vertical) {
        this.palette = palette;
        this.vertical = vertical;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    private BufferedImage preview() {
        BufferedImage previewer = new BufferedImage(1,256,BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 256; i++) {
            previewer.setRGB(0,i,palette.getTemperatureColorInt(i));
        }
        return previewer;
    }

    @Override
    public void run() {
        while (true) {

            Graphics g = getGraphics();
            if (g == null) {continue;}
            if (vertical) {
                g.drawImage(preview(), 2, 2, (int) (getWidth() * (2f / 3f)), getHeight() - 4, this);
                g.setColor(palette.getTemperatureColor(step));
                g.fillRect((int) (getWidth() * (2f / 3f)), 2, (int) (getWidth() * (1f / 3f)) - 1, getHeight() - 4);
                g.dispose();
            } else {
                g.drawImage(preview(), 2, 2, getWidth()-4, (int) (getHeight() * (4f / 5f)), this);
                g.setColor(palette.getTemperatureColor(step));
                g.fillRect(2, (int) (getHeight() * (4f / 5f))+2, getWidth() - 4,(int) (getHeight() * (4f / 5f))-1);
                g.dispose();
            }
            step = (step <= 0)?255:step-1;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
