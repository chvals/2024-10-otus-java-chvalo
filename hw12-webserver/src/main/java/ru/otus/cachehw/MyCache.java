package ru.otus.cachehw;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {

    Map<K, V> mapCache = new WeakHashMap<>();
    private final List<HwListener<K,V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        mapCache.put(key, value);
        notify(key, value, "Put into cache.");
    }

    @Override
    public void remove(K key) {
        if (mapCache.containsKey(key)) {
            V value = mapCache.remove(key);
            notify(key, value, "Remove from cache.");
        }
    }

    @Override
    public Optional<V> get(K key) {
        V value =  mapCache.get(key);
        if (value != null) {
            notify(key, value, "Get from cache.");
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notify(K key, V value, String action) {
        listeners.forEach(listener -> listener.notify(key, value, action));
    }
}
