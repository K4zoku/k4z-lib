package com.github.k4zoku.lib.event;

import java.util.function.Consumer;

public interface EventManager {
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback);
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority);
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority, boolean ignoreCancelled);
    void registerAll(EventListener listener);
    void unregister(EventListener listener);
    void unregisterAll(Class<? extends Event> eventType);
    void unregisterAll();
    void fire(Event event);
}
