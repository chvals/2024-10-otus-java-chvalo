/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.otus;

import com.google.common.base.Joiner;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String... args) {
        String[] strs = new String[] {"1", "2", "3"};
        System.out.println("Guava test join: " + Joiner.on("->").join(strs));
    }
}