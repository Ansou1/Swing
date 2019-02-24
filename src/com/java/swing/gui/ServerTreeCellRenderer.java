package com.java.swing.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.net.URL;

public class ServerTreeCellRenderer implements TreeCellRenderer {

    private JCheckBox leafRenderer;
    private DefaultTreeCellRenderer nonLeafRenderer;

    private Color textForeground;
    private Color textBackground;
    private Color selectionForeground;
    private Color selectionBackground;

    public ServerTreeCellRenderer() {
        this.leafRenderer = new JCheckBox();
        this.nonLeafRenderer = new DefaultTreeCellRenderer();

        nonLeafRenderer.setLeafIcon(createIcon("images/Server16.gif"));
        nonLeafRenderer.setOpenIcon(createIcon("images/WebComponent16.gif"));
        nonLeafRenderer.setClosedIcon(createIcon("images/WebComponentAdd16.gif"));

        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (leaf) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            ServerInfo nodeInfo = (ServerInfo)node.getUserObject();

            if (selected) {
                leafRenderer.setForeground(selectionForeground);
                leafRenderer.setBackground(selectionBackground);
            } else {
                leafRenderer.setForeground(textForeground);
                leafRenderer.setBackground(textBackground);
            }

            leafRenderer.setText(nodeInfo.toString());
            leafRenderer.setSelected(nodeInfo.getChecked());
            return leafRenderer;
        }
        else {
            return nonLeafRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
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
