package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper;

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.mapper = JsonMapper.builder().build();
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        // читает файл, парсит и возвращает результат
        ClassLoader classLoader = getClass().getClassLoader();
        var file = new File(classLoader.getResource(fileName).getFile());
        Measurement[] measurementArray = mapper.readValue(file, Measurement[].class);
        logger.info("Measurement[] from file {}: {}", fileName, measurementArray);
        return Arrays.asList(measurementArray);
    }
}
