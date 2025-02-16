package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleATM extends AbstractATM {
    private static final Logger logger = LoggerFactory.getLogger(SimpleATM.class);

    public SimpleATM(MoneyStorage moneyStorage) {
        super.moneyStorage = moneyStorage;
    }
    @Override
    public boolean put(List<Banknote> banknotes) {
        int giveSum = 0;
        for (Banknote banknote : banknotes) {
            Stack<Banknote> banknoteList = moneyStorage.getBox(banknote.getNominal());
            if (banknoteList == null) {
                logger.info("Банкноты  номиналом {} не принимаются!", banknote.getNominal().getValue());
               return false;
            }
            giveSum += banknote.getNominal().getValue();
            banknoteList.push(banknote);
        }
        logger.info("Приняты купюры на сумму = {}", giveSum);
        return true;
    }

    @Override
    public List<Banknote> get(int sum) {
        //список существующих номиналов в хранилище в порядке убывания
        List<BanknoteNominalEnum> nominalList = moneyStorage.getAllExistNominalBox().stream()
                .sorted(Comparator.comparingInt(BanknoteNominalEnum::getValue).reversed())
                .collect(Collectors.toList());

        //распределение банкнот к выдаче
        Map<BanknoteNominalEnum, Integer> banknoteMap = new HashMap<>();
        int curSum = sum;
        for (BanknoteNominalEnum nominal : nominalList) {
            int count = curSum / nominal.getValue();
            if (count > 0) {
                int boxSize = moneyStorage.getBox(nominal).size();
                int countBanknote = count <= boxSize ? count : boxSize;
                if (countBanknote > 0) {
                    banknoteMap.put(nominal, countBanknote);
                    curSum -= nominal.getValue() * countBanknote;
                }
            }
        }

        //формирование пачки банкнот к выдаче
        if (curSum == 0 && !banknoteMap.isEmpty()) {
            List<Banknote> banknotes = new ArrayList<>();
            for (Map.Entry<BanknoteNominalEnum, Integer> banknoteEntry: banknoteMap.entrySet()) {
                for (int i = 0; i < banknoteEntry.getValue(); i++) {
                   banknotes.add(moneyStorage.getBox(banknoteEntry.getKey()).pop());
                }
            }
            logger.info("К выдаче пачка банкнот: {}", banknotes.stream()
                    .sorted(Comparator.comparingInt(b -> b.getNominal().getValue()))
                    .collect(Collectors.toList()));
            return banknotes;
        } else {
            logger.info("нет возможности выдать заявленную сумму = {}", sum);
            return null;
        }

    }
}
