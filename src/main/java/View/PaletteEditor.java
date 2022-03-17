package View;

import Controller.FireController;
import Model.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.GridBagConstraints.*;

public class PaletteEditor extends JPanel {


    //Attributes


    private final FireController controller;
    private ColorSlot selectedColorSlot;

    private ArrayList<Color> baseColors = new ArrayList<>();


    //Components


    private final ColorSelector colorSelector;
    private final ArrayList<ColorSlot> baseColorSlots = new ArrayList<>();
    private final JPanel colorPanel = new JPanel();
    private final PalettePreviewer previewer;
    private final PaletteChooser paletteChooser;
    private final JButton addColor = new JButton("Add Color");
    private final JButton removeColor = new JButton("Remove Color");


    //Constructor


    public PaletteEditor(FireController c) {
        this.controller = c;
        this.colorSelector = new ColorSelector(c);
        Palette palette = c.getPaletteSaver().get("Standard");
        this.baseColors.addAll(Arrays.asList(palette.getBaseColors()));
        this.previewer = new PalettePreviewer(palette,true);
        this.paletteChooser = new PaletteChooser(controller);
        addComponents();
        addListeners();
    }


    //Methods


    private void addComponents() {
        addColor.setBackground(Color.white);
        colorSelector.setColor(baseColors.get(baseColors.size()-1));
        colorSelector.setBackground(controller.getBackgroundColor());
        setupColorPanel();
        new Thread(previewer).start();
        removeColor.setBackground(Color.white);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);
        add(colorPanel,gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        add(colorSelector,gbc);

        gbc.gridx = 1;
        add(new JSeparator(SwingConstants.VERTICAL),gbc);

        gbc.gridx = 2;
        gbc.gridheight = 1;
        add(paletteChooser,gbc);

        gbc.gridy = 2;
        add(new JSeparator(),gbc);

        gbc.gridy = 3;
        previewer.setPreferredSize(new Dimension(300,100));
        add(previewer,gbc);

        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JSeparator(),gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        add(addColor,gbc);

        gbc.gridx = 2;
        add(removeColor,gbc);


    }

    private void addListeners() {

        colorSelector.addChangeListener(e -> {
            Color c = colorSelector.getColor();
            selectedColorSlot.setColor(c);
            int index = baseColorSlots.indexOf(selectedColorSlot);
            baseColors.set(index,c);
            updatePalette();
        });

        addColor.addActionListener(e -> {
            if (baseColors.size()>=24) { return; }
            int index = baseColorSlots.indexOf(selectedColorSlot);
            baseColors.add(Color.white);
            for (int i = baseColors.size()-2; i > index; i--) {
                baseColors.set(i+1,baseColors.get(i));
            }
            baseColors.set(index+1,selectedColorSlot.getColor());
            Palette palette = new Palette(baseColors.toArray(new Color[baseColors.size()]));
            setPalette(palette);
            select(baseColorSlots.get(index+1));
        });

        removeColor.addActionListener(e -> {
            if (baseColors.size()<3) { return; }
            int index = baseColorSlots.indexOf(selectedColorSlot);
            baseColors.remove(index);
            Palette palette = new Palette(baseColors.toArray(new Color[baseColors.size()]));
            setPalette(palette);
            if (baseColorSlots.size() == index) {
                select(baseColorSlots.get(index-1));
            } else {
                select(baseColorSlots.get(index));
            }

        });

    }

    private void setupColorPanel() {
        colorPanel.setLayout(new GridBagLayout());
        colorPanel.setBackground(controller.getBackgroundColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        for (Color c : baseColors) {
            ColorSlot slot = createColorSlot(c);
            baseColorSlots.add(slot);
            colorPanel.add(slot,gbc);
            gbc.gridx++;
            if (gbc.gridx >= 12) {
                gbc.gridy++;
                gbc.gridx = 0;
            }
        }
        select(baseColorSlots.get(baseColorSlots.size()-1));
    }

    public ColorSlot createColorSlot(Color c) {
        ColorSlot slot = new ColorSlot(c);
        slot.addActionListener(e -> select(slot));
        return slot;
    }

    public void select(ColorSlot slot) {
        if (selectedColorSlot != null) {
            selectedColorSlot.unselect();
        }
        selectedColorSlot = slot;
        selectedColorSlot.select();
        Color c = slot.getColor();
        colorSelector.setColor(c);
    }

    public void updatePalette() {
        Color[] colorArray = new Color[baseColors.size()];
        for (int i = 0; i < baseColors.size(); i++) {
            colorArray[i] = baseColors.get(i);
        }
        Palette palette = new Palette(colorArray);
        controller.setFirePalette(palette);
        previewer.setPalette(palette);
    }

    public void setPalette(Palette palette) {
        baseColors = new ArrayList<>(Arrays.asList(palette.getBaseColors()));
        for (ColorSlot slot : baseColorSlots) {
            colorPanel.remove(slot);
        }
        updatePalette();
        baseColorSlots.clear();
        setupColorPanel();
        updateUI();
    }

    //Inner class


    private class ColorSlot extends JButton {


        //Attributes


        private Color color;

        private final Color SELECTED_COLOR = new Color(200,200,200);
        private final Color UNSELECTED_COLOR = new Color(250,250,250);


        //Constructor


        public ColorSlot(Color c) {
            setColor(c);
            setBackground(UNSELECTED_COLOR);
        }


        //Methods


        private void updateColor() {
            BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            int w = 20, h = 20;
            g.setColor(Color.black);
            g.fillRect(0,0,w,h);
            g.setColor(Color.white);
            g.fillRect(1,1,w-2,h-2);
            g.setColor(Color.lightGray);
            g.fillRect(1,1,w/2-1,h/2-1);
            g.fillRect(w/2,h/2, w/2-1, h/2-1);
            g.setColor(color);
            g.fillRect(1,1,w-2,h-2);

            image.flush();
            ImageIcon icon = new ImageIcon(image);
            setIcon(icon);
        }

        public Color getColor() {
            return color;
        }

        public void select() {
            setBackground(SELECTED_COLOR);
        }

        public void setColor(Color c) {
            color = c;
            updateColor();
        }

        public void unselect() {
            setBackground(UNSELECTED_COLOR);
        }

    }

}
