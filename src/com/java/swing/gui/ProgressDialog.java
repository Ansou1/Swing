package com.java.swing.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgressDialog extends JDialog {

    private JButton cancelButton;
    private JProgressBar progressBar;
    private ProgressDialogListener listener;

    public ProgressDialog(Window parent, String title) {

        super(parent, title, ModalityType.APPLICATION_MODAL);

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

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null)
                    listener.progressDialogCancelled();
            }
        });

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (listener != null)
                    listener.progressDialogCancelled();
            }
        });

        pack();

        setLocationRelativeTo(parent);
    }

    public void setListener(ProgressDialogListener listener) {
        this.listener = listener;
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
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setValue(0);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }

                ProgressDialog.super.setVisible(visible);
            }
        });
    }
}
