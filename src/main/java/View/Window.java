package View;

import Controller.FireController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.awt.GridBagConstraints.BOTH;

public class Window extends JFrame {


    //Attributes


    public static final String FIRE = "Fire", COOL = "Cooling Map", ORIG = "Original", CONV = "Convolution";
    private final FireController controller;


    //Components


    private final HashMap<String, Viewer> viewers = new HashMap<>();
    private final HashMap<String, Viewer> viewers2 = new HashMap<>();

    private final JTabbedPane viewerSelector = new JTabbedPane();
    private final JTabbedPane viewerSelector2 = new JTabbedPane();
    private final JTabbedPane controlPanelSelector = new JTabbedPane();


    //Constructor


    public Window(String title, FireController controller) throws HeadlessException {
        super(title);
        this.controller = controller;

        viewers.put(FIRE, new Viewer(controller.getFire()));
        viewers.put(COOL, new Viewer(controller.getCoolingMap()));
        viewers.put(ORIG, new Viewer(controller.getConvolution().getOriginalImage()));
        viewers.put(CONV, new Viewer(controller.getConvolution()));

        viewers2.put(FIRE, new Viewer(controller.getFire()));
        viewers2.put(COOL, new Viewer(controller.getCoolingMap()));
        viewers2.put(ORIG, new Viewer(controller.getConvolution().getOriginalImage()));
        viewers2.put(CONV, new Viewer(controller.getConvolution()));

        viewers.get(FIRE).setBackgroundImage(controller.getConvolution().getOriginalImage());
        viewers2.get(FIRE).setBackgroundImage(controller.getConvolution().getOriginalImage());

        setResizable(false);
        getContentPane().setBackground(controller.getBackgroundColor());
        getContentPane().setForeground(controller.getForegroundColor());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupControlPanelSelector();
        setupViewerSelectors();
        addComponents(getContentPane());

    }


    //Methods


    private void addComponents(Container c) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(3, 10, 10, 10);
        c.add(viewerSelector,gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridy = 1;
        c.add(new JSeparator(), gbc);
        gbc.insets = new Insets(3, 10, 10, 10);
        gbc.gridy = 2;
        c.add(viewerSelector2,gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.gridheight = 3;
        gbc.insets = new Insets(17, 10, 10, 10);
        c.add(controlPanelSelector,gbc);
    }

    private void addToControlPanel(String name, String iconFile, JPanel panel) {
        ImageIcon icon = new ImageIcon("src\\main\\resources\\Icons\\" + iconFile);
        controlPanelSelector.addTab(name, icon, panel);
        panel.setBackground(controller.getBackgroundColor());
    }

    private void addToViewerSelector(String name, Viewer viewer) {
        viewerSelector.addTab(name, viewer);
        viewer.setBackground(controller.getBackgroundColor());
    }

    private void addToViewerSelector2(String name, Viewer viewer) {
        viewerSelector2.addTab(name, viewer);
        viewer.setBackground(controller.getBackgroundColor());
    }

    public void open() {
        setVisible(true);

        int w = 0, h = 0;

        for (String key : viewers.keySet()) {
            w = Math.max(w, viewers.get(key).getImage().getWidth());
            h = Math.max(h, viewers.get(key).getImage().getHeight());
        }

        for (String key : viewers.keySet()) {
            viewers.get(key).setSize(w,h);
            viewers2.get(key).setSize(w,h);
        }

        pack();

        for (String key : viewers.keySet()) {
            new Thread(viewers.get(key)).start();
            new Thread(viewers2.get(key)).start();
        }
    }

    private void setupControlPanelSelector() {

        controlPanelSelector.setBackground(controller.getBackgroundColor());
        controlPanelSelector.setForeground(controller.getForegroundColor());
        FireEditor fireEditor = new FireEditor(controller);
        addToControlPanel("Fire", "fire.png", fireEditor);

        addToControlPanel("Palette", "palette.png", new PaletteEditor(controller));
        addToControlPanel("Export", "export.png", new ExporterPanel(controller));
        addToControlPanel("Advanced", "advanced.png", new AdvancedSettings(controller));
        addToControlPanel("Convolution", "convolution.png", new ConvolutionPanel(controller));


    }

    private void setupViewerSelectors() {

        viewerSelector.setBackground(controller.getBackgroundColor());
        viewerSelector.setForeground(controller.getForegroundColor());
        viewerSelector2.setBackground(controller.getBackgroundColor());
        viewerSelector2.setForeground(controller.getForegroundColor());

        viewerSelector.addChangeListener(e ->{
            Viewer actual = (Viewer) viewerSelector.getComponent(viewerSelector.getSelectedIndex());
            actual.setSize(actual.getImage().getWidth(), actual.getImage().getHeight());
            if (isVisible()) {pack();}
        });

        viewerSelector2.addChangeListener(e ->{
            Viewer actual = (Viewer) viewerSelector2.getComponent(viewerSelector2.getSelectedIndex());
            actual.setSize(actual.getImage().getWidth(), actual.getImage().getHeight());
            if (isVisible()) {pack();}
        });


        for (String key : viewers.keySet()) {
            addToViewerSelector(key, viewers.get(key));
            addToViewerSelector2(key, viewers2.get(key));
        }

    }

}
