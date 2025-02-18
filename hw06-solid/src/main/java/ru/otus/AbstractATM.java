package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public int getBalans() {
        int balans = 0;
        for (BanknoteNominalEnum nominal : moneyStorage.getAllExistNominalBox()) {
            balans += nominal.getValue() * moneyStorage.getBox(nominal).size();
        }
        return balans;
    }
}
