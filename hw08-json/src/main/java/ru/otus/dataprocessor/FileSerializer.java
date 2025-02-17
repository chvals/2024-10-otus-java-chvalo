package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final ObjectMapper mapper;
    private final String fileName;

    public FileSerializer(String fileName) {
        this.mapper = JsonMapper.builder().build();
        this.fileName = fileName;

    }
    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        // формирует результирующий json и сохраняет его в файл
        var file = new File(fileName);
        mapper.writeValue(file, data);
    }
}
