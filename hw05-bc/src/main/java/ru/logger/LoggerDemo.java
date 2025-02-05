package ru.logger;

public class LoggerDemo {
    public static void main(String[] args) {
        MyClassInterface myClass = Ioc.createMyClass();
        System.out.println("-------------------------------------");
        System.out.println("2 + 2 = " + myClass.add(2,2));
        System.out.println("-------------------------------------");
        System.out.println("2 + 2 + 2 = " + myClass.add(2,2, 2));
        System.out.println("-------------------------------------");
        System.out.println("2 + 2 = " + myClass.add(2,2));
    }
}
