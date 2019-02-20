package com.java.swing.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.nio.file.Path;

public class Toolbar extends JToolBar implements ActionListener {
    private ToolbarListener textListener;
    private JButton saveButton;
    private JButton refreshButton;

    public Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());
        //setFloatable(false);

        saveButton = new JButton("");
        refreshButton = new JButton("");

        saveButton.setIcon(createIcon("images/Save16.gif"));
        refreshButton.setIcon(createIcon("images/Refresh16.gif"));

        saveButton.addActionListener(this);
        refreshButton.addActionListener(this);

        saveButton.setToolTipText("save");
        refreshButton.setToolTipText("refresh");

        add(saveButton);
        //addSeparator();
        add(refreshButton);
    }

    public void setToolbarListener(ToolbarListener listener) {
        this.textListener = listener;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton)e.getSource();
        if (clicked == saveButton) {
            if (textListener != null) {
                textListener.saveEventOccured();
            }
        }
        else {
            if (textListener != null) {
                textListener.refreshEventOccured();
            }
        }
    }
}
