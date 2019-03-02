package com.java.swing.gui;

import com.java.swing.controller.Controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class MainFrame extends JFrame {

    private Toolbar toolbar;
    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private Controller controller;
    private TablePanel tablePanel;
    private PrefsDialog prefsDialog;
    private Preferences prefs;
    private JSplitPane splitPane;
    private JTabbedPane jTabbedPane;
    private MessagePanel messagePanel;

    public MainFrame() {
        super("Hello world!!!");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        formPanel = new FormPanel();

        controller = new Controller();

        tablePanel = new TablePanel();
        tablePanel.setData(controller.getPeople());
        tablePanel.setPersonTableListener(new PersonTableListenener() {
            public void rowDeleted(int row) {
                controller.removePerson(row);
            }
        });

        jTabbedPane = new JTabbedPane();
        messagePanel = new MessagePanel(this);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, jTabbedPane);
        splitPane.setOneTouchExpandable(true);

        jTabbedPane.addTab("Person Database", tablePanel);
        jTabbedPane.addTab("Messages", messagePanel);

        jTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int tabIndex = jTabbedPane.getSelectedIndex();
                if (tabIndex == 1) {
                    messagePanel.refresh();
                }
            }
        });

        prefs = Preferences.userRoot().node("db");

        prefsDialog = new PrefsDialog(this);
        prefsDialog.setPrefsListener(new PrefsListener() {
            @Override
            public void preferencesSet(String user, String password, int port) {
                prefs.put("user", user);
                prefs.put("password", password);
                prefs.putInt("port", port);

                try {
                    controller.configure(port, user, password);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Unable to re-connect");
                }

            }
        });

        String user = prefs.get("user", "");
        String password = prefs.get("password", "");
        Integer port = prefs.getInt("port", 3306);
        prefsDialog.setDefaults(user, password, port);

        try {
            controller.configure(port, user, password);
        } catch (Exception e) {
            System.err.println("Cannot connect to database");
        }

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new PersonFileFilter());

        setJMenuBar(createMenuBar());

        toolbar.setToolbarListener(new ToolbarListener() {
            @Override
            public void saveEventOccured() {
                System.out.println("Save");
                connect();
                try {
                    controller.save();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Cannot save to database", "Database save Fail", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void refreshEventOccured() {
                refresh();
            }
        });

        formPanel.setFormListener(new FormListener () {
            public void formEventOccured(FormEvent e) {
                controller.addPerson(e);
                tablePanel.refresh();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing");
                controller.disconnect();
                dispose(); // Dispose the window, quit automatic
                System.gc(); //Garbage collector for stacktrace problem when closing, error input to swing
            }
        });

        add(toolbar, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);

        refresh();

        setMinimumSize(new Dimension(600, 500));
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void connect() {
        try {
            controller.connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Cannot connect to database", "Database connection Fail", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exportDataItem = new JMenuItem("Export data...");
        JMenuItem importDataItem = new JMenuItem("Import data...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(exportDataItem);
        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu windowMenu = new JMenu("Window");
        JMenu showMenu = new JMenu("Show");

        JMenuItem prefsItem = new JMenuItem("Preferences");

        JMenuItem showFormItem = new JCheckBoxMenuItem("Person Form");
        showFormItem.setSelected(true);

        showMenu.add(showFormItem);
        windowMenu.add(showMenu);
        windowMenu.add(prefsItem);

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);

        prefsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prefsDialog.setVisible(true);
            }
        });

        showFormItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)e.getSource();

                if (menuItem.isSelected()) {
                    splitPane.setDividerLocation((int)formPanel.getMinimumSize().getWidth());
                }
                formPanel.setVisible(menuItem.isSelected());
            }
        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.META_MASK));

        importDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.loadFromFile(fileChooser.getSelectedFile());
                        tablePanel.refresh();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not load data from file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        exportDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.saveToFile(fileChooser.getSelectedFile());
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Could not save data to file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to exit the application?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    WindowListener[] listeners = getWindowListeners();

                    for (WindowListener listener : listeners) {
                        listener.windowClosing(new WindowEvent(MainFrame.this, 0));
                    }
                }
            }
        });

        return menuBar;
    }

    public void refresh() {
        connect();
        try {
            controller.load();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Cannot load from database", "Database load Fail", JOptionPane.ERROR_MESSAGE);
        }
        tablePanel.refresh();
    }
}
