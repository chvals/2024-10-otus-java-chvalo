package ru.otus.model;

import java.util.Arrays;
import java.util.List;

public class ObjectForMessage implements Copyable<ObjectForMessage>{
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.copyOf(this.data));
        return objectForMessage;
    }
}
