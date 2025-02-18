package ru.otus;

import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class MoneyStorage {
    private Map<BanknoteNominalEnum, Stack<Banknote>> storage;

    public MoneyStorage(Map<BanknoteNominalEnum, Stack<Banknote>> storage) {
        this.storage = storage;
    }

    public Stack<Banknote> getBox(BanknoteNominalEnum nominal) {
        return storage.get(nominal);
    }

    public List<BanknoteNominalEnum> getAllExistNominalBox() {
        return storage.keySet().stream().collect(Collectors.toList());
    }
}
