package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorChangeSwapField11Field12 implements Processor{
    @Override
    public Message process(Message message) {
        return message.toBuilder().field12(message.getField11()).field11(message.getField12()).build();
    }
}
