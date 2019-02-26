package com.java.swing.gui;

import com.java.swing.controller.MessageServer;
import com.java.swing.model.Message;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class MessagePanel extends JPanel implements ProgressDialogListener{

    private JTree serverTree;
    private ServerTreeCellRenderer treeCellRenderer;
    private ServerTreeCellEditor treeCellEditor;
    private ProgressDialog progressDialog;

    private Set<Integer> selectedServers;

    private MessageServer messageServer;

    private SwingWorker<List<Message>, Integer> worker;

    public MessagePanel(JFrame parent) {

        progressDialog = new ProgressDialog(parent, "Messages Downloading...");
        messageServer = new MessageServer();

        progressDialog.setListener(this);

        selectedServers = new TreeSet<Integer>();

        selectedServers.add(0);
        selectedServers.add(1);
        selectedServers.add(4);

        treeCellRenderer = new ServerTreeCellRenderer();
        treeCellEditor = new ServerTreeCellEditor();

        serverTree = new JTree(createTree());
        serverTree.setCellRenderer(treeCellRenderer);
        serverTree.setCellEditor(treeCellEditor);
        serverTree.setEditable(true);

        serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        treeCellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                ServerInfo info = (ServerInfo)treeCellEditor.getCellEditorValue();
                System.out.println(info + ": " + info.getId() + ", " + info.getChecked());
                int serverID = info.getId();
                if (info.getChecked()) {
                    selectedServers.add(serverID);
                } else {
                    selectedServers.remove(serverID);
                }
                messageServer.setSelectedServers(selectedServers);

                retrieveMessages();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        setLayout(new BorderLayout());

        add(new JScrollPane(serverTree), BorderLayout.CENTER);
    }


    private void retrieveMessages() {

        progressDialog.setMaximum(messageServer.getMessageCount());
        progressDialog.setVisible(true);

        worker = new SwingWorker<List<Message>, Integer>() {
            @Override
            protected List<Message> doInBackground() throws Exception {
                List<Message> retrievedMessages = new ArrayList<>();

                int count = 0;

                for (Message message : messageServer) {
                    if (isCancelled()) break;

                    System.out.println(message.getTitle());
                    retrievedMessages.add(message);
                    count++;
                    publish(count);
                }
                return retrievedMessages;
            }

            @Override
            protected void process(List<Integer> count) {
                int retrieved = count.get(count.size() - 1);
                progressDialog.setValue(retrieved);
                System.out.println("Current Retrieved: " + retrieved);
            }

            @Override
            protected void done() {

                progressDialog.setVisible(false);

                if (isCancelled()) return;

                try {
                    List<Message> retrievedMessage = get();
                    System.out.println("Retrieved " + retrievedMessage.size() + " messages.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private DefaultMutableTreeNode createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("PLACES");

        DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
        DefaultMutableTreeNode leave1 = new DefaultMutableTreeNode(new ServerInfo("NEW YORK", 0, selectedServers.contains(0)));
        DefaultMutableTreeNode leave2 = new DefaultMutableTreeNode(new ServerInfo("LAS VEGAS", 1, selectedServers.contains(1)));
        DefaultMutableTreeNode leave3 = new DefaultMutableTreeNode(new ServerInfo("WASHINGTON", 2, selectedServers.contains(2)));

        branch1.add(leave1);
        branch1.add(leave2);
        branch1.add(leave3);

        DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
        DefaultMutableTreeNode leave4 = new DefaultMutableTreeNode(new ServerInfo("LONDON", 3, selectedServers.contains(3)));
        DefaultMutableTreeNode leave5 = new DefaultMutableTreeNode(new ServerInfo("MANCHESTER", 4, selectedServers.contains(4)));
        DefaultMutableTreeNode leave6 = new DefaultMutableTreeNode(new ServerInfo("SOUTHAMPTON", 5, selectedServers.contains(5)));

        branch2.add(leave4);
        branch2.add(leave5);
        branch2.add(leave6);

        DefaultMutableTreeNode branch3 = new DefaultMutableTreeNode("FRANCE");
        DefaultMutableTreeNode leave7 = new DefaultMutableTreeNode(new ServerInfo("PARIS", 6, selectedServers.contains(6)));
        DefaultMutableTreeNode leave8 = new DefaultMutableTreeNode(new ServerInfo("LYON", 7, selectedServers.contains(7)));
        DefaultMutableTreeNode leave9 = new DefaultMutableTreeNode(new ServerInfo("TOULOUSE", 8, selectedServers.contains(8)));

        branch3.add(leave7);
        branch3.add(leave8);
        branch3.add(leave9);

        root.add(branch1);
        root.add(branch2);
        root.add(branch3);

        return root;
    }

    @Override
    public void progressDialogCancelled() {
        if (worker != null) {
            worker.cancel(true);
        }
    }
}
