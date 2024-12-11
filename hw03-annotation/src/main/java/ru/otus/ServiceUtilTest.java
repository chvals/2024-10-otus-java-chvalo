package ru.otus;

import java.io.*;

public class ServiceUtilTest {

    BufferedReader reader;
    StringBuilder stringBuilder;
    ServiceUtil service;

    @Before
    void beforeEach() throws IOException {
        service = new ServiceUtil();
        stringBuilder = new StringBuilder();
        File file = new File("hw03-annotation/src/main/resources/test.txt");
        reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
    }

    @After
    void afterEach() throws IOException {
        reader.close();
    }

    @Test
    void test_max() throws InvalidTestException {
        int maxElement = service.getMax(stringBuilder.toString(), ";");
        if (maxElement != 10) {
            throw new InvalidTestException(String.format("actual: %s and expected: %s", maxElement, 10));
        }
    }

    @Test
    void test_min() throws InvalidTestException {
        int minElement = service.getMin(stringBuilder.toString(), ";");
        if (minElement != 1) {
            throw new InvalidTestException(String.format("actual: %s and expected: %s", minElement, 1));
        }
    }

    @Test
    void test_size() throws InvalidTestException {
        int count = service.getCount(stringBuilder.toString(), ";");
        if (count != 10) {
            throw new InvalidTestException(String.format("actual: %s and expected: %s", count, 10));
        }
    }

}
