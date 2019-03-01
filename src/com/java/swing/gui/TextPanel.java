package com.java.swing.gui;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private JTextArea textArea;

    public TextPanel() {
        textArea = new JTextArea();

        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        textArea.setFont(new Font("Serif", Font.PLAIN, 20));

        setLayout(new BorderLayout());

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String string) {
        textArea.append(string);
    }

    public void setText(String text) {
        this.textArea.setText(text);
    }

}
