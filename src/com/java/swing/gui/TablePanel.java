package com.java.swing.gui;

import com.java.swing.model.EmploymentCategory;
import com.java.swing.model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TablePanel extends JPanel{

    private JTable table;
    private PersonTableModel tableModel;
    private JPopupMenu popup;
    private PersonTableListenener personTableListener;

    public TablePanel() {
        tableModel = new PersonTableModel();
        table = new JTable(tableModel);
        popup = new JPopupMenu();

        table.setDefaultRenderer(EmploymentCategory.class, new EmployementCategoryRenderer());
        table.setDefaultEditor(EmploymentCategory.class, new EmploymentCategoryEditor());
        table.setRowHeight(20);

        JMenuItem removeItem = new JMenuItem("Delete row");
        popup.add(removeItem);

        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (personTableListener != null)
                    personTableListener.rowDeleted(row);
                tableModel.fireTableRowsDeleted(row, row);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());

                table.getSelectionModel().setSelectionInterval(row, row);

                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(table, e.getX(), e.getY());
                }
            }
        });

        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(List<Person> db) {
        tableModel.setData(db);
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }

    public void setPersonTableListener(PersonTableListenener listener) {
        this.personTableListener = listener;
    }
}