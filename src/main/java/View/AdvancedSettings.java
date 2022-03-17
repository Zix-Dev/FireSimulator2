package View;

import Controller.FireController;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class AdvancedSettings extends JPanel {


    //Attributes


    private final FireController controller;


    //Components


    private final KernelSelector kernelSelector;
    private final JSlider coolingPower = new JSlider(0,30,10);
    private final JButton copyPalette = new JButton("Copy Palette to Cooling Map");
    private final JButton convection = new JButton("Disable Convection");


    //Constructor


    public AdvancedSettings(FireController controller) {
        this.controller = controller;
        this.kernelSelector = new KernelSelector(controller);
        addComponents();
        addListeners();
    }


    //Mathods


    private void addComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);
        JLabel aux = new JLabel("Pixel Average Calculation: ");
        aux.setForeground(controller.getForegroundColor());
        add(aux,gbc);

        gbc.gridx = 1;
        gbc.fill = NONE;
        add(kernelSelector,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = BOTH;
        add(new JSeparator(),gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 0;
        aux = new JLabel("Cooling: ");
        aux.setForeground(controller.getForegroundColor());
        add(aux,gbc);

        gbc.gridx = 1;
        coolingPower.setBackground(controller.getBackgroundColor());
        coolingPower.setPaintLabels(true);
        coolingPower.setToolTipText("The most cold, the quickest the fire will extinguish");
        coolingPower.createToolTip();
        add(coolingPower,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = BOTH;
        add(new JSeparator(),gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        copyPalette.setBackground(controller.getButtonColor());
        copyPalette.setForeground(controller.getForegroundColor());
        copyPalette.setToolTipText("Changes are only visible with a cooling map enabled");
        copyPalette.createToolTip();
        add(copyPalette, gbc);

        gbc.gridx = 1;
        convection.setBackground(controller.getButtonColor());
        convection.setForeground(controller.getForegroundColor());
        convection.setToolTipText("Convections is the property of a fire of moving up");
        convection.createToolTip();
        add(convection, gbc);

    }

    private void addListeners() {

        coolingPower.addChangeListener(e -> {
            controller.setCoolingPower(coolingPower.getValue());
        });

        copyPalette.addActionListener(e -> {
            controller.getCoolingMap().setPalette(controller.getFire().getPalette());
        });

        convection.addActionListener(e -> {
            if (controller.getConfig().convectionEnabled) {
                convection.setText("Enable Convection");
                controller.convectionSetEnabled(false);
                convection.setBackground(controller.getSelectedButtonColor());
            } else {
                convection.setText("Disable Convection");
                controller.convectionSetEnabled(true);
                convection.setBackground(controller.getButtonColor());
            }
        });

    }


    //InnerClasses


    public class KernelSelector extends JPanel {

        JCheckBox[][] kernel = new JCheckBox[5][5];

        public KernelSelector(FireController controller) {
            setLayout(new GridBagLayout());
            for (int x = 0; x < kernel.length; x++) {
                for (int y = 0; y < kernel[0].length; y++) {
                    addCheckBox(y,x,controller.getConfig().getKernel()[x][y]);
                }
            }
            kernel[2][2].setBackground(new Color(20,20,20));
        }

        private void addCheckBox(int x, int y, boolean selected) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weighty = 1;
            gbc.weightx = 1;
            gbc.fill = BOTH;
            gbc.gridx = x;
            gbc.gridy = y;
            kernel[y][x] = new JCheckBox();
            add(kernel[y][x],gbc);
            kernel[y][x].setSelected(selected);
            kernel[y][x].setBackground(controller.getBackgroundColor());
            kernel[y][x].addActionListener(e -> {
                boolean s = kernel[x][y].isSelected();
                if (!s && trueCount() < 1) {
                    kernel[y][x].setSelected(true);
                    return;
                }
                controller.setFireKernel(getKernel());
            });
            kernel[y][x].setToolTipText("Each fire pixel is calculated by an average of the selected relative coordinates");
            kernel[y][x].createToolTip();
        }

        public int trueCount() {
            int count = 0;
            for (int x = 0; x < kernel.length; x++) {
                for (int y = 0; y < kernel[0].length; y++) {
                    if (kernel[x][y].isSelected()) {count++;}
                }
            }
            return count;
        }

        @Override
        public String toString() {
            String s = "";
            for (int x = 0; x < kernel.length; x++) {
                s = s.concat("\n");
                for (int y = 0; y < kernel[0].length; y++) {
                    s = s.concat((kernel[x][y].isSelected())?"O ":"X ");
                }
            }
            return s;
        }

        public boolean[][] getKernel() {
            boolean[][] k = new boolean[5][5];
            for (int x = 0; x < kernel.length; x++) {
                for (int y = 0; y < kernel[0].length; y++) {
                    k[x][y] = kernel[x][y].isSelected();
                }
            }
            return k;
        }
    }

}
