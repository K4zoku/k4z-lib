package com.github.k4zoku.lib.event;

public interface EventExecutor {
    void execute(EventListener listener, Event event) throws EventException;
}
