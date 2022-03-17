package View;

import Model.TImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Viewer extends Canvas implements Runnable {


    //Attributes


    private final BufferedImage image;
    private BufferedImage background;


    //Constructor


    public Viewer(BufferedImage image) {
        this.image = image;
        this.background = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
    }


    //Methods


    public BufferedImage getBackgroundImage() {
        return background;
    }

    public void setBackgroundImage(BufferedImage image) {
        background = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void update() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(background,0,0,this);
        g.drawImage(image,0,0,this);
        g.dispose();
        bs.show();
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            update();
            try {
                sleep(12);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
