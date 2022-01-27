package com.github.k4zoku.lib.event;

/**
 * Event interface, all events must implement this interface
 * to be able to be handled by the EventManager.
 */
public interface Event {

    /**
     * Get the event's name. This is used for logging and debugging.
     * <p>
     * Default implementation returns the class name.
     *
     * @return The event's name.
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Get {@link EventHandlerList HandlerList} which holds all
     * registered {@link EventHandler EventHandlers} of this event.
     *
     * @return HandlerList
     */
    EventHandlerList getHandlers();

}
