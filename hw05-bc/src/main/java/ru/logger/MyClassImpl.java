package ru.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClassImpl implements MyClassInterface {

    @Log
    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int add(int x, int y, int z) {
        return x + y + z;
    }
}
