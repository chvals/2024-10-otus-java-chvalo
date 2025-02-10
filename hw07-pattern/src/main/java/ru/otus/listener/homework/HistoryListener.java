package ru.otus.listener.homework;

import java.util.*;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.otus.listener.Listener;
import ru.otus.model.Message;


public class HistoryListener implements Listener, HistoryReader {

    private Map<Long,Message> messageMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageMap.put(msg.getId(), msg.copy());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageMap.get(id));
    }
}
