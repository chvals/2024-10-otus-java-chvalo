package ru.otus.webserver.services;

import java.io.IOException;
import java.util.Map;

public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
    String getPageSimple(String filename) throws IOException;
}
