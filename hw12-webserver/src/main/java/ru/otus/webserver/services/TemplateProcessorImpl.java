package ru.otus.webserver.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {

    private final Configuration configuration;

    public TemplateProcessorImpl(String templatesDir) {
        configuration = new Configuration(Configuration.VERSION_2_3_30);
        // configuration.setDirectoryForTemplateLoading(new File(templatesDir));  // for directory
        configuration.setClassForTemplateLoading(this.getClass(), templatesDir); // for resource
        configuration.setDefaultEncoding("UTF-8");
    }

    @Override
    public String getPage(String filename, Map<String, Object> data) throws IOException {
        try (Writer stream = new StringWriter()) {
            Template template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String getPageSimple(String filename) throws IOException {
        try (InputStream stream = new FileInputStream(filename)) {
            stream.toString();
            return stream.toString();
        }
    }
}
