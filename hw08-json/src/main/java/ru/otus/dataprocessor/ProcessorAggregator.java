package ru.otus.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorAggregator.class);
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        Map<String, Double> result = data.stream()
                .collect(Collectors.groupingBy(Measurement::name, TreeMap::new, Collectors.summingDouble(Measurement::value)));
        logger.info("ProcessorAggregator process result: {}", result);
        return result;
    }
}
