package View;

import Controller.FireController;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;

public class FireEditor extends JPanel {

    //Attributes


    private final FireController controller;


    //Components


    private final JLabel igniter = new JLabel("Igniter: ");
    private final JButton memoryIgniter = new JButton("Igniter With Memory");
    private final JButton randomIgniter = new JButton("Random Igniter");
    private final JButton lineIgniter = new JButton("Line Igniter");
    private final JButton convolutionIgniter = new JButton("Convolution Igniter");
    private final JLabel coolingMap = new JLabel("Cooling Type");
    private final JButton noneCooling = new JButton("Without Cooling Map");
    private final JButton randomCooling = new JButton("Random Cooling Map");
    private final JButton importedCooling = new JButton("Imported Cooling Map");
    private final JButton pause = new JButton("Pause");
    private final LabeledComponent<JSlider> speed = new LabeledComponent<>("Speed: ",new JSlider(0,39,20),LabeledComponent.VERTICAL);
    private  final LabeledComponent<JSlider> igniterIntensity = new LabeledComponent<>("Igniter Intensity: ",new JSlider(0,15,10),LabeledComponent.VERTICAL);


    //Constructor


    public FireEditor(FireController controller) {
        this.controller = controller;
        addComponents();
        addColors();
        addListeners();
    }


    //Methods


    private void addComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5,5,5,5);
        add(igniter,gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(memoryIgniter,gbc);

        gbc.gridx = 1;
        add(randomIgniter,gbc);

        gbc.gridx = 2;
        add(lineIgniter,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(convolutionIgniter,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(new JSeparator(), gbc);

        gbc.gridy = 4;
        add(coolingMap, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(noneCooling,gbc);

        gbc.gridx = 1;
        add(randomCooling,gbc);

        gbc.gridx = 2;
        add(importedCooling,gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        add(new JSeparator(), gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        add(pause,gbc);

        gbc.gridx = 1;
        add(speed,gbc);

        gbc.gridx = 2;
        add(igniterIntensity,gbc);
    }

    private void addListeners() {

        memoryIgniter.setToolTipText("The sparks of the fire will remember their latest position and temperature");
        memoryIgniter.createToolTip();
        memoryIgniter.addActionListener(e -> {
            if (controller.getConfig().getIgniterType() == 2) {
                return;
            }
            memoryIgniter.setBackground(controller.getSelectedButtonColor());
            randomIgniter.setBackground(controller.getButtonColor());
            lineIgniter.setBackground(controller.getButtonColor());
            convolutionIgniter.setBackground(controller.getButtonColor());
            controller.setIgniterType(2);
        });

        randomIgniter.setToolTipText("Random sparks will be generated at the bottom of the fire");
        randomIgniter.createToolTip();
        randomIgniter.addActionListener(e -> {
            if (controller.getConfig().getIgniterType() == 1) {
                return;
            }
            randomIgniter.setBackground(controller.getSelectedButtonColor());
            memoryIgniter.setBackground(controller.getButtonColor());
            lineIgniter.setBackground(controller.getButtonColor());
            convolutionIgniter.setBackground(controller.getButtonColor());
            controller.setIgniterType(1);
        });

        lineIgniter.setToolTipText("The fire sparks will be generated all over the bottom");
        lineIgniter.createToolTip();
        lineIgniter.addActionListener(e -> {
            if (controller.getConfig().getIgniterType() == 0) {
                return;
            }
            lineIgniter.setBackground(controller.getSelectedButtonColor());
            randomIgniter.setBackground(controller.getButtonColor());
            memoryIgniter.setBackground(controller.getButtonColor());
            convolutionIgniter.setBackground(controller.getButtonColor());
            controller.setIgniterType(0);
        });

        convolutionIgniter.setToolTipText("The fire sparks will be generated all over the bottom");
        convolutionIgniter.createToolTip();
        convolutionIgniter.addActionListener(e -> {
            if (controller.getConfig().getIgniterType() == 3) {
                return;
            }
            convolutionIgniter.setBackground(controller.getSelectedButtonColor());
            randomIgniter.setBackground(controller.getButtonColor());
            memoryIgniter.setBackground(controller.getButtonColor());
            lineIgniter.setBackground(controller.getButtonColor());
            controller.setIgniterType(3);
        });

        noneCooling.setToolTipText("Without the cooling map, the fire cooling will be flat");
        noneCooling.createToolTip();
        noneCooling.addActionListener(e -> {
            if (controller.getConfig().getCoolingType() == 0) {
                return;
            }
            noneCooling.setBackground(controller.getSelectedButtonColor());
            randomCooling.setBackground(controller.getButtonColor());
            importedCooling.setBackground(controller.getButtonColor());
            controller.setCoolingType(0);
            importedCooling.setText("Imported Cooling Map");
        });

        randomCooling.setToolTipText("Generates a random noisy cooling map");
        randomCooling.createToolTip();
        randomCooling.addActionListener(e -> {
            if (controller.getConfig().getCoolingType() == 1) {
                return;
            }
            randomCooling.setBackground(controller.getSelectedButtonColor());
            noneCooling.setBackground(controller.getButtonColor());
            importedCooling.setBackground(controller.getButtonColor());
            controller.setCoolingType(1);
            importedCooling.setText("Imported Cooling Map");
        });

        importedCooling.setToolTipText("Imports a cooling map from the CoolingMaps folder");
        importedCooling.createToolTip();
        importedCooling.addActionListener(e -> {
            if (controller.getConfig().getCoolingType() == 2) {
                controller.setNextImportedCoolingMap();
                return;
            }
            importedCooling.setBackground(controller.getSelectedButtonColor());
            randomCooling.setBackground(controller.getButtonColor());
            noneCooling.setBackground(controller.getButtonColor());
            controller.setCoolingType(2);
            importedCooling.setText("Next Imported Cooling Map");
        });

        pause.addActionListener(e -> {
            if (controller.getConfig().paused) {
                pause.setText("Pause");
                controller.getConfig().paused = false;
                pause.setBackground(controller.getButtonColor());
            } else {
                pause.setText("Resume");
                controller.getConfig().paused = true;
                pause.setBackground(controller.getSelectedButtonColor());
            }
        });

        speed.getComponent().addChangeListener(e -> controller.setUpdateDelay(40 - speed.getComponent().getValue()));

        igniterIntensity.setToolTipText("Generates a random noisy cooling map");
        igniterIntensity.createToolTip();
        igniterIntensity.getComponent().addChangeListener(e -> {
            controller.setIgniterIntensity(igniterIntensity.getComponent().getValue());
        });
    }

    private void addColors() {

        Color foreground = controller.getForegroundColor();
        Color button = controller.getButtonColor();
        Color selectedButton = controller.getSelectedButtonColor();

        igniter.setForeground(foreground);

        memoryIgniter.setForeground(foreground);
        memoryIgniter.setBackground(selectedButton);

        randomIgniter.setForeground(foreground);
        randomIgniter.setBackground(button);

        lineIgniter.setForeground(foreground);
        lineIgniter.setBackground(button);

        convolutionIgniter.setForeground(foreground);
        convolutionIgniter.setBackground(button);

        coolingMap.setForeground(foreground);

        noneCooling.setForeground(foreground);
        noneCooling.setBackground(selectedButton);

        randomCooling.setForeground(foreground);
        randomCooling.setBackground(button);

        importedCooling.setForeground(foreground);
        importedCooling.setBackground(button);

        igniterIntensity.setForegroundColor(foreground);
        igniterIntensity.setBackgroundColor(controller.getBackgroundColor());

        pause.setForeground(foreground);
        pause.setBackground(button);

        speed.setBackgroundColor(controller.getBackgroundColor());
        speed.setForegroundColor(foreground);
    }


}
