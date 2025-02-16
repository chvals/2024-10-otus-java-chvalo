package ru.otus.processor;
import ru.otus.model.Message;

import java.time.LocalDateTime;


public class ProcessorGenExceprionBySec implements Processor{
    private final DateTimeProvider dateTimeProvider;

    public ProcessorGenExceprionBySec(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2  == 0) {
            throw new ProcessorException("Четная секунда!");
        }
        return message;
    }
}
