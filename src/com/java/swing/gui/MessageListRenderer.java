package com.java.swing.gui;

import com.java.swing.model.Message;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MessageListRenderer implements ListCellRenderer {

    private JPanel panel;
    private JLabel label;

    private Color selectedColor;
    private Color normalColor;

    public MessageListRenderer() {
        panel = new JPanel();
        label = new JLabel();

        selectedColor = new Color(210, 210, 255);
        normalColor = Color.WHITE;

        label.setIcon(createIcon("images/Information24.gif"));

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        panel.add(label);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Message message = (Message)value;

        label.setText(message.getTitle());

        panel.setBackground(cellHasFocus ? selectedColor : normalColor);

        return panel;
    }

    private ImageIcon createIcon(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null)
                System.err.println("unable to load image: " + path);
            else
                return new ImageIcon(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
