package com.java.swing.controller;

import com.java.swing.model.Message;

import java.util.*;
import java.util.function.Consumer;

public class MessageServer implements Iterable<Message> {

    private Map<Integer, List<Message>> messages;

    private List<Message> selected;

    public MessageServer() {
        selected = new ArrayList<Message>();
        messages = new TreeMap<Integer, List<Message>>();

        List<Message> list = new ArrayList<Message>();

        list.add(new Message("The cat is missing", "Have you seen Felix anywhere?"));
        list.add(new Message("see you later?", "Are we still meeting in the pub"));
        messages.put(0, list);

        list = new ArrayList<Message>();
        list.add(new Message("Jagi is Napa", "Why all korean are Napa?"));
        list.add(new Message("Where are you coming from?", "I come from France"));
        messages.put(1, list);
    }

    public void setSelectedServers(Set<Integer> serversID) {
        selected.clear();
        for (Integer id : serversID) {
            if (messages.containsKey(id)) {
                selected.addAll(messages.get(id));
            }
        }
    }

    public int getMessageCount() {
        return selected.size();
    }

    @Override
    public Iterator<Message> iterator() {
        return new MessageIterator(selected);
    }
}

class MessageIterator implements Iterator {

    private Iterator<Message> iterator;

    public MessageIterator(List<Message> messages) {
        iterator = messages.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}