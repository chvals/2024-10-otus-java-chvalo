package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) throws ContextException {
        for (int i = 0; i < initialConfigClasses.length; i++) {
            checkConfigClass(initialConfigClasses[i]);
        }

        List<Class<?>> configClassList = Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());
        for (Class<?> clazz : configClassList) {
            try {
                processConfig(clazz);
            } catch (Exception e) {
                throw new ContextException(e.getMessage());
            }
        }
    }

    public AppComponentsContainerImpl(String packagePath) throws ContextException {
        Reflections reflections = new Reflections(packagePath);
        List<Class<?>> classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream()
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());
        if (classes.isEmpty()) throw new ContextException("Not found config classes!");
        for (Class<?> clazz : classes) {
            try {
                processConfig(clazz);
            } catch (Exception e) {
                throw new ContextException(e.getMessage());
            }
        }
    }

    private void processConfig(Class<?> configClass)
            throws ContextException, InvocationTargetException, IllegalAccessException, NoSuchMethodException,
                    InstantiationException {
        List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .peek(m -> m.setAccessible(true))
                .sorted(Comparator.comparingInt(
                        f -> f.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        Object confObject = configClass.getConstructor().newInstance();
        for (Method method : methods) {
            Object object;
            Class<?>[] methodParams = method.getParameterTypes();
            if (methodParams.length > 0) {
                List<Object> argList = new ArrayList<>();
                for (int i = 0; i < methodParams.length; i++) {
                    argList.add(getAppComponent(methodParams[i]));
                }
                object = method.invoke(confObject, argList.toArray());
            } else {
                object = method.invoke(confObject);
            }
            appComponents.add(object);
            String compName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(compName)) {
                throw new ContextException("Bean by name: " + compName + " already exist!");
            }
            appComponentsByName.put(compName, object);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws ContextException {
        List<Object> objects = appComponents.stream()
                .filter(f -> componentClass.isAssignableFrom(f.getClass()))
                .collect(Collectors.toList());
        if (objects.isEmpty()) throw new ContextException("Not found bean by class: " + componentClass.getName());
        if (objects.size() > 1)
            throw new ContextException("Found more then 1 bean by class: " + componentClass.getName());
        return (C) objects.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) throws ContextException {
        if (!appComponentsByName.containsKey(componentName))
            throw new ContextException("Not found bean by name: " + componentName);
        return (C) appComponentsByName.get(componentName);
    }
}
