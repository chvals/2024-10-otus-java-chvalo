package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new ArrayBlockingQueue<>(bufferSize, true);
    }

    @Override
    public void process(SensorData data) {
        var pushResult = dataBuffer.offer(data);
        if (!pushResult) {
            log.warn("Буффер переполнен. Запись.");
            flush();
            dataBuffer.offer(data);
        }
    }

    public synchronized void flush() {
        List<SensorData> bufferedData = new ArrayList<>();
        try {
            while (dataBuffer.size() > 0) {
                bufferedData.add(dataBuffer.take());
            }
            if (bufferedData.size() > 0) {
                Collections.sort(bufferedData, Comparator.comparing(SensorData::getMeasurementTime));
                writer.writeBufferedData(bufferedData);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
