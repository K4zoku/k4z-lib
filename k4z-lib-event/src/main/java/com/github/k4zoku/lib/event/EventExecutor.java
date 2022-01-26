package com.github.k4zoku.lib.event;

public interface EventExecutor {
    void execute(Listener listener, Event event) throws EventException;
}
