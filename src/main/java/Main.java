import Controller.FireController;
import Model.*;
import View.*;


public class Main {

    public static void main(String[] args) {

        Fire f = new Fire(650,250);
        FireController c = new FireController(f);
        c.setFirePalette("Standard");

        Window w = new Window("Fire", c);

        new Thread(f).start();
        w.open();

    }

}
