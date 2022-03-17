package View;

import Controller.FireController;
import Model.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;

class PaletteChooser extends JPanel {


    //Attributes


    private boolean removing = false;
    private final FireController controller;
    private ArrayList<String> paletteNames = new ArrayList<>();


    //Components


    private final JComboBox<ImageIcon> paletteList = new JComboBox<>();
    private final LabeledComponent<JTextField> nameField = new LabeledComponent<>("Name: ", new JTextField(), LabeledComponent.HORIZONTAL);
    private final JButton saveButton = new JButton("Save Palette");
    private final JButton deleteButton = new JButton("Remove Palette");


    //Constructor


    public PaletteChooser(FireController controller) {
        this.controller = controller;
        getPalettes();
        addComponents();
        addListeners();
        setBackground(controller.getBackgroundColor());
        setForeground(controller.getForegroundColor());
    }


    //Methods


    private void getPalettes() {
        for (String s : controller.getPaletteSaver().getPaletteNames()) {
            addPalette(s);
        }
    }

    private void addComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.fill = BOTH;
        gbc.insets = new Insets(5,5,5,5);
        paletteList.setBackground(Color.white);
        add(paletteList,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        saveButton.setBackground(Color.white);
        add(saveButton,gbc);

        gbc.gridx = 1;
        deleteButton.setBackground(Color.white);
        add(deleteButton,gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        nameField.setBackgroundColor(controller.getBackgroundColor());
        nameField.setForegroundColor(controller.getForegroundColor());
        add(nameField,gbc);

    }

    private void addListeners() {
        paletteList.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED || removing) { return; }
            String selected = paletteNames.get(paletteList.getSelectedIndex());
            Palette palette = controller.getPaletteSaver().get(selected);
            ((PaletteEditor) getParent()).setPalette(palette);
        });

        saveButton.addActionListener(e -> {
            if (nameField.getComponent().getText().isBlank() || isRepeated(nameField.getComponent().getText())) { return; }
            Palette palette = controller.getFire().getPalette();
            String name = nameField.getComponent().getText();
            controller.savePalette(palette, name);
            addPalette(name);
            paletteList.setSelectedItem(paletteList.getItemAt(paletteList.getItemCount()-1));
            nameField.getComponent().setText("");
        });

        deleteButton.addActionListener(e -> {
            int index = paletteList.getSelectedIndex();
            String selected = paletteNames.get(index);
            if (selected.equals("Standard")||selected.equals("Gray")) {return;}
            removing = true;
            paletteList.setSelectedItem(paletteList.getItemAt(0));
            paletteList.removeItemAt(index);
            paletteNames.remove(index);
            controller.removePalette(selected);
            removing = false;
        });
    }

    public void addPalette(String name) {
        paletteNames.add(name);
        paletteList.addItem(controller.getPaletteSaver().get(name).toIcon(name));
    }

    public boolean isRepeated(String name) {
        for (String s : paletteNames) {
            if (s.equals(name)) {return true;}
        }
        return false;
    }


}


