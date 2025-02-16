package ru.otus;

import java.util.List;

public abstract class AbstractATM implements BaseATM {
    protected MoneyStorage moneyStorage;

    @Override
    public boolean put(List<Banknote> banknotes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Banknote> get(int sum) {
        throw new UnsupportedOperationException();
    }
}
