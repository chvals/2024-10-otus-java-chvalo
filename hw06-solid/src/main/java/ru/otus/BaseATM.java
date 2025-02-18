package ru.otus;

import java.util.List;

public interface BaseATM {
    boolean put(List<Banknote> banknotes);
    List<Banknote> get(int sum);
    int getBalans();
}
