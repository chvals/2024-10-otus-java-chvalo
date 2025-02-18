package ru.otus;


public enum BanknoteNominalEnum {
    rub100(100),
    rub200(200),
    rub500(500),
    rub1000(1000),
    rub5000(5000);

    private int value;
    BanknoteNominalEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
