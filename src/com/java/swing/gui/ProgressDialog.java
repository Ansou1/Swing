package com.java.swing.gui;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {

    private JButton cancelButton;
    private JProgressBar progressBar;

    public ProgressDialog(Window parent) {

        super(parent, "Message downloading...", ModalityType.APPLICATION_MODAL);

        cancelButton = new JButton("Cancel");
        progressBar = new JProgressBar();

        progressBar.setStringPainted(true);
        //progressBar.setString("Retrieving messages...");

        setLayout(new FlowLayout());

        Dimension size = cancelButton.getPreferredSize();
        size.width = 400;
        progressBar.setPreferredSize(size);

        add(progressBar);
        add(cancelButton);

        pack();

        setLocationRelativeTo(parent);
    }

    public void setMaximum(int value) {
        progressBar.setMaximum(value);
    }

    public void setValue(int value) {
        int progress = (value * 100) / progressBar.getMaximum();
        progressBar.setString(String.format("%d%% complete", progress));
        progressBar.setValue(value);
    }

    @Override
    public void setVisible(final boolean visible) {
        progressBar.setValue(0);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                if (visible == false) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setValue(0);
                }

                ProgressDialog.super.setVisible(visible);
            }
        });
    }
}
