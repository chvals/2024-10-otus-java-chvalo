package ru.otus;

import java.util.*;

public class HomeWorkMain {
    public static void main(String[] args) {

        //сформируем хранилище с ячейками нужных номиналов купюр
        Map<BanknoteNominalEnum, Stack<Banknote>> mapStorage = new HashMap<>();
        mapStorage.put(BanknoteNominalEnum.rub100, new Stack<>());
        mapStorage.put(BanknoteNominalEnum.rub200, new Stack<>());
        mapStorage.put(BanknoteNominalEnum.rub500, new Stack<>());
        mapStorage.put(BanknoteNominalEnum.rub1000, new Stack<>());
        mapStorage.put(BanknoteNominalEnum.rub5000, new Stack<>());
        MoneyStorage moneyStorage = new MoneyStorage(mapStorage);

        //создадим банкомат с хранилищем
        AbstractATM simpleATM = new SimpleATM(moneyStorage);

        //положим пачку купюр в банкомат
        List<Banknote> banknoteList = new ArrayList<>();
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub100));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub100));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub200));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub200));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub500));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub500));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub1000));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub1000));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub5000));
        banknoteList.add(new Banknote(BanknoteNominalEnum.rub5000));
        simpleATM.put(banknoteList);

        //снимем определенную сумму
        simpleATM.get(12900);
        simpleATM.get(1000);
    }
}
