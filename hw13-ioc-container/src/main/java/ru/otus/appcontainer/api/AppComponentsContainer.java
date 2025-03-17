package ru.otus.appcontainer.api;

import ru.otus.appcontainer.ContextException;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass) throws ContextException;

    <C> C getAppComponent(String componentName) throws ContextException;
}
