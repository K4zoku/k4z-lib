package com.github.k4zoku.lib.event;

import java.util.function.Consumer;

/**
 * EventManager is a class that manages events and their listeners.
 */
public interface EventManager {

    /**
     * Registers a simple listener for the specified event type.
     *
     * @param eventType The event type to listen for.
     * @param callback The callback to call when the event is fired.
     * @param <T> The event type.
     * @return The listener, which can be used to unregister it.
     */
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback);

    /**
     * Registers a simple listener for the specified event type, with the specified priority.
     *
     * @param eventType The event type to listen for.
     * @param callback The callback to call when the event is fired.
     * @param priority The priority of the listener.
     * @param <T> The event type.
     * @return The listener, which can be used to unregister it.
     */
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority);

    /**
     * Registers a simple listener for the specified event type, with the specified priority,
     * and a boolean flag to indicate this listener ignores cancelled events.
     *
     * @param eventType The event type to listen for.
     * @param callback The callback to call when the event is fired.
     * @param priority The priority of the listener.
     * @param ignoreCancelled Whether this listener ignores cancelled events.
     * @param <T> The event type.
     * @return The listener, which can be used to unregister it.
     */
    <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority, boolean ignoreCancelled);

    /**
     * Register all listeners in the specified class.
     *
     * @param listener The class to register listeners from.
     */
    void registerAll(EventListener listener);

    /**
     * Unregisters the specified listener.
     *
     * @param listener The listener to unregister.
     */
    void unregister(EventListener listener);

    /**
     * Unregisters all listeners of the specified event type.
     *
     * @param eventType The event type to unregister listeners from.
     */
    void unregisterAll(Class<? extends Event> eventType);

    /**
     * Unregisters all listeners.
     */
    void unregisterAll();

    /**
     * Fires the specified event.
     *
     * @param event The event to fire.
     */
    void fire(Event event);

}
