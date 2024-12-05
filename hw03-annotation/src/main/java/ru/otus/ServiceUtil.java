package ru.otus;

import java.util.Arrays;

public class ServiceUtil {

    public int getMax(String str, String delimeter) {
        return Arrays.asList(str.split(delimeter))
                .stream()
                .map(s -> Integer.valueOf(s.trim()))
                .max(Integer::compare)
                .get();
    }

    public int getMin(String str, String delimeter) {
        return Arrays.asList(str.split(delimeter))
                .stream()
                .map(s -> Integer.valueOf(s.trim()))
                .min(Integer::compare)
                .get();
    }

    public int getCount(String str, String delimeter) {
        return Arrays.asList(str.split(delimeter)).size();
    }

}
