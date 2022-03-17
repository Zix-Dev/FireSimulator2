package View;

import Controller.FireController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;

public class ColorSelector extends JPanel {


    //Attributes


    private Color color;
    private final FireController controller;


    //Components


    private final JColorChooser colorChooser = new JColorChooser();
    private final LabeledComponent<JSlider> alpha = new LabeledComponent<>("Alpha: ", new JSlider(0,255,255), LabeledComponent.HORIZONTAL);
    private final LabeledComponent<JSpinner> alphaSpinner = new LabeledComponent<>("A: ", new JSpinner(), LabeledComponent.HORIZONTAL);
    private final LabeledComponent<JSpinner> redSpinner = new LabeledComponent<>("R: ", new JSpinner(), LabeledComponent.HORIZONTAL);
    private final LabeledComponent<JSpinner> greenSpinner = new LabeledComponent<>("G: ", new JSpinner(), LabeledComponent.HORIZONTAL);
    private final LabeledComponent<JSpinner> blueSpinner = new LabeledComponent<>("B: ", new JSpinner(), LabeledComponent.HORIZONTAL);


    //Constructor


    public ColorSelector(FireController controller) {
        this.controller = controller;
        this.color = Color.white;
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
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        setupColorChooser();
        add(colorChooser, gbc);

        gbc.gridy = 1;
        add(alpha,gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(alphaSpinner, gbc);

        gbc.gridx = 1;
        add(redSpinner, gbc);

        gbc.gridx = 2;
        add(greenSpinner, gbc);

        gbc.gridx = 3;
        add(blueSpinner, gbc);

    }

    private void addColors() {
        alpha.setBackgroundColor(controller.getBackgroundColor());
        alpha.setForegroundColor(controller.getForegroundColor());

        alphaSpinner.setBackgroundColor(controller.getBackgroundColor());
        alphaSpinner.setForegroundColor(controller.getForegroundColor());

        redSpinner.setBackgroundColor(controller.getBackgroundColor());
        redSpinner.setForegroundColor(controller.getForegroundColor());

        greenSpinner.setBackgroundColor(controller.getBackgroundColor());
        greenSpinner.setForegroundColor(controller.getForegroundColor());

        blueSpinner.setBackgroundColor(controller.getBackgroundColor());
        blueSpinner.setForegroundColor(controller.getForegroundColor());

    }

    private void addListeners() {

        colorChooser.getSelectionModel().addChangeListener(e ->{
            Color c = colorChooser.getColor();
            redSpinner.getComponent().setValue(c.getRed());
            greenSpinner.getComponent().setValue(c.getGreen());
            blueSpinner.getComponent().setValue(c.getBlue());
            color = new Color(c.getRed(),c.getGreen(),c.getBlue(), alpha.getComponent().getValue());
        });

        alpha.getComponent().addChangeListener(e ->{
            alphaSpinner.getComponent().setValue(alpha.getComponent().getValue());
            color = new Color(color.getRed(),color.getGreen(), color.getBlue(), alpha.getComponent().getValue());
        });

        alphaSpinner.getComponent().addChangeListener(e -> {
            int value = (int) alphaSpinner.getComponent().getValue();
            value = (value < 0)?255:(value > 255)?0:value;
            alphaSpinner.getComponent().setValue(value);
            color = new Color(color.getRed(),color.getGreen(),color.getBlue(),value);
            alpha.getComponent().setValue(value);
        });

        redSpinner.getComponent().addChangeListener(e -> {
            int value = (int) redSpinner.getComponent().getValue();
            value = (value < 0)?255:(value > 255)?0:value;
            redSpinner.getComponent().setValue(value);
            color = new Color(value ,color.getGreen(), color.getBlue(), color.getAlpha());
            colorChooser.setColor(color);
        });

        greenSpinner.getComponent().addChangeListener(e -> {
            int value = (int) greenSpinner.getComponent().getValue();
            value = (value < 0)?255:(value > 255)?0:value;
            greenSpinner.getComponent().setValue(value);
            color = new Color(color.getRed() , value, color.getBlue(), color.getAlpha());
            colorChooser.setColor(color);
        });

        blueSpinner.getComponent().addChangeListener(e -> {
            int value = (int) blueSpinner.getComponent().getValue();
            value = (value < 0)?255:(value > 255)?0:value;
            blueSpinner.getComponent().setValue(value);
            color = new Color(color.getRed() ,color.getGreen(), value, color.getAlpha());
            colorChooser.setColor(color);
        });
    }

    private void setupColorChooser() {
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[0]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[1]);
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.setColor(Color.white);
        colorChooser.getChooserPanels()[0].getComponent(0).setVisible(false);
        colorChooser.getChooserPanels()[0].getComponent(0).setEnabled(false);

        colorChooser.getChooserPanels()[0].getParent().getParent().setBackground(controller.getBackgroundColor());
        colorChooser.getChooserPanels()[0].setBackground(controller.getBackgroundColor());

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        alpha.getComponent().setValue(color.getAlpha());
        colorChooser.setColor(color);
        colorChooser.getSelectionModel().setSelectedColor(color);
        redSpinner.getComponent().setValue(color.getRed());
        greenSpinner.getComponent().setValue(color.getGreen());
        blueSpinner.getComponent().setValue(color.getBlue());
        alphaSpinner.getComponent().setValue(color.getAlpha());
    }

    public void addChangeListener(ChangeListener l) {
        colorChooser.getSelectionModel().addChangeListener(l);
        alpha.getComponent().addChangeListener(l);
        alphaSpinner.getComponent().addChangeListener(l);
        redSpinner.getComponent().addChangeListener(l);
        greenSpinner.getComponent().addChangeListener(l);
        blueSpinner.getComponent().addChangeListener(l);
    }

}
