package com.java.swing.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrefsDialog extends JDialog {
    private JButton okButton;
    private JButton cancelButton;
    private JSpinner portSpinner;
    private SpinnerNumberModel spinnerNumberModel;
    private JTextField userField;
    private JPasswordField passField;
    private PrefsListener prefsListener;

    public PrefsDialog(JFrame parent) {
        super(parent, "Preferences", false);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        spinnerNumberModel = new SpinnerNumberModel(3306, 0, 9999, 1);
        portSpinner = new JSpinner(spinnerNumberModel);

        userField = new JTextField(10);
        passField = new JPasswordField(10);

        passField.setEchoChar('*');

        layoutControls();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer port = (Integer)portSpinner.getValue();
                String user = userField.getText();
                char[] password = passField.getPassword();
                if (prefsListener != null) {
                    prefsListener.preferencesSet(user, new String(password), port);
                }
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        setSize(new Dimension(350, 250));
        setLocationRelativeTo(parent);
    }

    public void setDefaults(String user, String password, int port) {
        userField.setText(user);
        passField.setText(password);
        portSpinner.setValue(port);
    }

    public void setPrefsListener(PrefsListener prefsListener) {
        this.prefsListener = prefsListener;
    }

    private void layoutControls() {

        JPanel controlPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        Border spaceBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border titleBorder = BorderFactory.createTitledBorder("Database Preferences");


        controlPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

        controlPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        Insets rightPadding = new Insets(0,0,0, 15);
        Insets noPadding = new Insets(0,0,0, 0);

        gc.gridy = 0;
        gc.gridx = 0;

        gc.weightx = 1;
        gc.weighty = 1;

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlPanel.add(new JLabel("User: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlPanel.add(userField, gc);

        /////////////////////////////////////

        gc.gridy++;
        gc.gridx = 0;

        gc.weightx = 1;
        gc.weighty = 1;

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlPanel.add(new JLabel("Password: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlPanel.add(passField, gc);

        /////////////////////////////////////

        gc.gridy++;
        gc.gridx = 0;

        gc.weightx = 1;
        gc.weighty = 1;

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = rightPadding;
        controlPanel.add(new JLabel("Port: "), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = noPadding;
        controlPanel.add(portSpinner, gc);

        ////////////////////////////////////
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        gc.gridy++;
        gc.gridx = 0;

        buttonsPanel.add(okButton);
        gc.gridx++;
        buttonsPanel.add(cancelButton);

        Dimension btnSize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(btnSize);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

    }
}
