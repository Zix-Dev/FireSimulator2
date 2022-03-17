package View;

import Controller.FireController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import static java.awt.GridBagConstraints.BOTH;

public class ExporterPanel extends JPanel {


    //Attributes


    private final FireController controller;


    //Components


    private final JLabel head = new JLabel("Export Fire to .gif: ");
    private final JFileChooser fileChooser = new JFileChooser();
    private final LabeledComponent<JTextField> fileName = new LabeledComponent<>("File name: ", new JTextField(), LabeledComponent.HORIZONTAL);
    private final LabeledComponent<JSpinner> duration = new LabeledComponent<>("Duration (seconds): ", new JSpinner(), LabeledComponent.HORIZONTAL);
    private final JButton export = new JButton("Export");
    private final JProgressBar bar = new JProgressBar();


    //Constructors


    public ExporterPanel(FireController controller) {
        this.controller = controller;
        addComponents();
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
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5,5,5);
        head.setForeground(controller.getForegroundColor());
        add(head,gbc);

        gbc.gridy = 1;
        fileChooser.getComponent(3).setVisible(false);
        fileChooser.getComponent(0).setBackground(controller.getBackgroundColor());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        add(fileChooser,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        fileName.setForegroundColor(controller.getForegroundColor());
        fileName.setBackgroundColor(controller.getBackgroundColor());
        add(fileName,gbc);

        gbc.gridx = 1;
        duration.getComponent().setValue(1);
        duration.setForegroundColor(controller.getForegroundColor());
        duration.setBackgroundColor(controller.getBackgroundColor());
        add(duration,gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        export.setBackground(controller.getButtonColor());
        export.setForeground(controller.getForegroundColor());
        add(export,gbc);

        gbc.gridy = 6;
        bar.setBackground(controller.getSelectedButtonColor());
        bar.setForeground(controller.getButtonColor());
        bar.setString("Exporting... Please Wait...");
        bar.setStringPainted(true);
        bar.setVisible(false);
        add(bar,gbc);
    }

    private void addListeners() {
        duration.getComponent().addChangeListener(e -> {
            if ((int)duration.getComponent().getValue() < 1) {
                duration.getComponent().setValue(1);
            }
        });

        export.addActionListener(e -> new Thread(()->{
           int seconds =  (int)duration.getComponent().getValue();
           String name = fileName.getComponent().getText();
           String dir = fileChooser.getCurrentDirectory().getPath();
           if (seconds < 1 || name.isBlank()) {
               return;
           }
           export.setVisible(false);
           bar.setVisible(true);
           Window w = (Window) getParent().getParent().getParent().getParent().getParent();
           w.setEnabled(false);
           String path = dir + "\\" + name + ".gif";
            try {
                controller.export(path, seconds, bar);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            w.setEnabled(true);
            bar.setValue(0);
            duration.getComponent().setValue(1);
            fileName.getComponent().setText("");
            export.setVisible(true);
            bar.setVisible(false);
        }).start());

    }


}
