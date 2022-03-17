package View;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.RELATIVE;


class LabeledComponent<T extends Component> extends JPanel {


    public static final boolean VERTICAL = true, HORIZONTAL = false;


    //Components


    JLabel label;
    T component;


    //Constructor


    public LabeledComponent(String name, T component, boolean orientation) {
        this.label = new JLabel(name);
        this.component = component;
        setLayout(new GridBagLayout());
        addComponents(orientation);
    }

    public LabeledComponent(JLabel label, T component, boolean orientation) {
        this.label = label;
        this.component = component;
        setLayout(new GridBagLayout());
        addComponents(orientation);
    }


    //Getters


    public JLabel getLabel() {
        return label;
    }

    public T getComponent() {
        return component;
    }


    //Methods


    public void addComponents(boolean orientation) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        if (orientation == VERTICAL) {
            gbc.gridy = RELATIVE;
        } else {
            gbc.gridx = RELATIVE;
        }
        add(label, gbc);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;
        add(component, gbc);
    }

    public void setBackgroundColor(Color color) {
        setBackground(color);
        label.setBackground(color);
        component.setBackground(color);
    }

    public void setForegroundColor(Color color) {
        setForeground(color);
        label.setForeground(color);
        component.setForeground(color);
    }

}