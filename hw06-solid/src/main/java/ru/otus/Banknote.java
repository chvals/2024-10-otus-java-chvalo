package ru.otus;

import java.util.UUID;

public class Banknote {
    private BanknoteNominalEnum nominal;
    private String serialNumber;

    public Banknote(BanknoteNominalEnum nominal) {
        this.nominal = nominal;
        this.serialNumber = UUID.randomUUID().toString();
    }

    public BanknoteNominalEnum getNominal() {
        return nominal;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banknote banknote = (Banknote) o;

        if (nominal != banknote.nominal) return false;
        return serialNumber != null ? serialNumber.equals(banknote.serialNumber) : banknote.serialNumber == null;
    }

    @Override
    public int hashCode() {
        int result = nominal != null ? nominal.hashCode() : 0;
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "nominal=" + nominal.getValue() +
                //", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
