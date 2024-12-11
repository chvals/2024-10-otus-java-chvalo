package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFrameworkDemo {
    private static final Logger logger = LoggerFactory.getLogger(TestFrameworkDemo.class);
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String classTestName = "ru.otus.ServiceUtilTest";
        try {
            TestFrameworkDemo.startTestByClassName(classTestName);
        } catch (ClassNotFoundException | InstantiationException e) {
            logger.info("Ошибка запуска тестов: {}", e.getMessage());
        }
    }

    public static void startTestByClassName(String classTestName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(classTestName);
        List<String> testMethods = new ArrayList<>();
        String beforeMethodName = "";
        String afterMethodName = "";
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method.getName());
            }
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethodName = method.getName();
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethodName = method.getName();
            }
        }

        //Тестируем
        logger.info("Test start...");
        int testCount = testMethods.size();
        int testErrorCount = 0;
        for (String testMethodName : testMethods) {
            Object testObj = clazz.getConstructor().newInstance();
            try {
                //before
                if (!beforeMethodName.isEmpty()) {
                    Method beforeMethod = testObj.getClass().getDeclaredMethod(beforeMethodName);
                    beforeMethod.invoke(testObj);
                }
                //test
                Method testMethod = testObj.getClass().getDeclaredMethod(testMethodName);
                testMethod.invoke(testObj);
                //after
                if (!afterMethodName.isEmpty()) {
                    Method afterMethod = testObj.getClass().getDeclaredMethod(afterMethodName);
                    afterMethod.invoke(testObj);
                }
                logger.info("Test " + testMethodName + " - OK!");
            } catch (Exception e) {
                if (((InvocationTargetException) e).getTargetException() instanceof InvalidTestException) {
                    logger.info("Test {} - not successful! {}", testMethodName, ((InvocationTargetException) e).getTargetException().getMessage());
                } else {
                    logger.info("Start test {} - error! {}", testMethodName, ((InvocationTargetException) e).getTargetException().getMessage());
                }
                testErrorCount++;
            }

        }
        logger.info("Start test = {} (test successful = {}, test not successful = {})", testCount, testErrorCount, testCount - testErrorCount);
    }
}