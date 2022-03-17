package View;

import Controller.FireController;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.VERTICAL;

public class ConvolutionPanel extends JPanel {


    //Attributes


    private final FireController controller;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JPanel settings = new JPanel();

    private ConvolutionKernel convolutionKernel = new ConvolutionKernel(2);
    private final JButton kernel3 = new JButton("3x3");
    private final JButton kernel5 = new JButton("5x5");
    private final JButton kernel7 = new JButton("7x7");
    private final JButton kernel9 = new JButton("9x9");
    private final JButton kernelReset = new JButton("Reset");
    private final JSlider threshold = new JSlider(VERTICAL,0,255, 255);
    JLabel thresholdLabel = new JLabel("Threshold");

    private final JPanel importer = new JPanel();

    //Constructor


    public ConvolutionPanel(FireController controller) {

        this.controller = controller;
        addComponents();
        addListeners();
        addColors();

    }


    //Methods


    private void addColors() {
        Color bg = controller.getBackgroundColor();
        Color fg = controller.getForegroundColor();
        Color btn = controller.getButtonColor();

        tabbedPane.setBackground(bg);
        tabbedPane.setForeground(fg);
        settings.setBackground(bg);
        settings.setForeground(fg);
        importer.setBackground(bg);
        importer.setForeground(fg);

        convolutionKernel.setBackground(bg);

        thresholdLabel.setForeground(fg);
        threshold.setBackground(bg);

        kernel3.setBackground(btn);
        kernel3.setForeground(bg);
        kernel5.setBackground(btn);
        kernel5.setForeground(bg);
        kernel7.setBackground(btn);
        kernel7.setForeground(bg);
        kernel9.setBackground(btn);
        kernel9.setForeground(bg);
        kernelReset.setBackground(btn);
        kernelReset.setForeground(bg);
    }

    private void addComponents() {
        tabbedPane.addTab("Settings", settings);
        tabbedPane.addTab("Import", importer);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);

        add(tabbedPane,gbc);

        addComponentsSettings();

    }

    private void addComponentsSettings() {
        settings.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 2;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);

        settings.add(convolutionKernel, gbc);

        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = NONE;
        thresholdLabel.setHorizontalAlignment(CENTER);
        settings.add(thresholdLabel,gbc);

        gbc.fill = BOTH;
        gbc.gridy = 1;
        settings.add(threshold, gbc);

        gbc.gridx = 0;
        gbc.weighty = 0.2f;
        gbc.weightx = 0.2f;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        settings.add(new JSeparator(),gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;
        settings.add(kernel3,gbc);

        gbc.gridx = 1;
        settings.add(kernel5,gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        settings.add(kernel7,gbc);

        gbc.gridx = 1;
        settings.add(kernel9,gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        settings.add(new JSeparator(), gbc);

        gbc.gridy = 6;
        settings.add(kernelReset, gbc);

    }

    private void addListeners() {

        ChangeListener convolutionListener = e -> {
            float[][] kernel = convolutionKernel.getKernel();
            controller.setConvolutionKernel(kernel);
        };

        convolutionKernel.addChangeListener(convolutionListener);

        threshold.addChangeListener(e -> controller.setConvolutionThreshold(255 - threshold.getValue()));

        kernel3.addActionListener(e -> changeKernel(2, convolutionListener));

        kernel5.addActionListener(e -> changeKernel(3, convolutionListener));

        kernel7.addActionListener(e -> changeKernel(4, convolutionListener));

        kernel9.addActionListener(e -> changeKernel(5, convolutionListener));

        kernelReset.addActionListener(e -> changeKernel(convolutionKernel.getKernel().length/2 + 1, convolutionListener));
    }

    private void changeKernel(int radius, ChangeListener listener) {
        settings.remove(convolutionKernel);
        convolutionKernel = new ConvolutionKernel(radius);
        controller.getConvolution().setKernel(convolutionKernel.getKernel());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 2;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);

        settings.add(convolutionKernel, gbc);
        convolutionKernel.addChangeListener(listener);
        convolutionKernel.setBackground(controller.getBackgroundColor());
        updateUI();
    }


    //InnerClass


    public class ConvolutionKernel extends JPanel {


        //Attributes


        private final float[][] kernel;
        private final JSpinner[][] spinners;
        private JSpinner divisor;


        //Constructor


        public ConvolutionKernel(int radius) {
            if (radius < 0) {throw new IllegalArgumentException();}
            int d = (radius*2)-1;
            this.spinners = new JSpinner[d][d];
            this.kernel = new float[d][d];
            setLayout(new GridBagLayout());
            addComponents();
            spinners[spinners.length/2][spinners.length/2].setValue(1f);
            kernel[spinners.length/2][spinners.length/2] = 1f;
            setPreferredSize(new Dimension(150,150));
        }


        //Methods


        private void addComponents() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = BOTH;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.insets = new Insets(1,1,1,1);
            int X = 0, Y = 0;
            Dimension size = new Dimension(150/spinners.length -2,150/spinners.length -2);
            this.divisor = new JSpinner(new SpinnerNumberModel(1f,0f,100f,0.1f));
            divisor.setPreferredSize(size);
            for (int x = -1; ++x< spinners.length;) {
                for (int y = -1; ++y< spinners.length;) {

                    gbc.gridx = x;
                    gbc.gridy = y;
                    spinners[x][y] = new JSpinner(new SpinnerNumberModel(0f,-50f,50f,0.1f));
                    spinners[x][y].setPreferredSize(size);
                    add(spinners[x][y],gbc);
                    Y = y;
                }
                X = x;
            }
            gbc.gridx = X+1;
            gbc.gridy = Y/2;
            add(divisor,gbc);
        }

        private void updateKernel() {
            for (int x = 0; x < kernel.length; x++) {
                for (int y = 0; y < kernel.length; y++) {
                    Object value = spinners[x][y].getValue();
                    float v  = 0;
                    if (value instanceof Double) {
                        double value2 = (double) value;
                        v = (float) value2;
                    } else if (value instanceof Float) {
                        v = (float) value;
                    }

                    Object divisor = this.divisor.getValue();
                    float d = 1;
                    if (divisor instanceof Double) {
                        double divisor2 = (double) divisor;
                        d = (float) divisor2;
                    } else if (divisor instanceof Float) {
                        d = (float) divisor;
                    }

                    kernel[x][y] = v/d;


                }
            }
        }

        public void addChangeListener(ChangeListener listener) {
            for (JSpinner[] sl : spinners) {
                for (JSpinner s : sl) {
                    s.addChangeListener(listener);
                }
            }
            divisor.addChangeListener(listener);
        }

        public float[][] getKernel() {
            updateKernel();
            return kernel;
        }

    }

}
