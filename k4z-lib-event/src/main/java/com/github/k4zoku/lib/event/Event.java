package com.github.k4zoku.lib.event;

public interface Event {

    default String getName() {
        return getClass().getSimpleName();
    }

    EventHandlerList getHandlers();

}
