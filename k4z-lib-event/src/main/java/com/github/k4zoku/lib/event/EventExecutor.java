package com.github.k4zoku.lib.event;

public interface EventExecutor {
    /**
     * Executes the event.
     *
     * @param listener The listener that is executing the event.
     * @param event The event to execute.
     * @throws EventException If an exception occurs while executing the event.
     */
    void execute(EventListener listener, Event event) throws EventException;
}
