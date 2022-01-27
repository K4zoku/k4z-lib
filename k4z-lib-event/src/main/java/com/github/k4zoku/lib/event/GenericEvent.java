package com.github.k4zoku.lib.event;

import java.util.Map;

public class GenericEvent implements Event {

    private static final EventHandlerList HANDLER_LIST = new EventHandlerList();

    private final String eventName;
    private final Map<String, Object> data;

    public GenericEvent(String eventName, Map<String, Object> data) {
        this.eventName = eventName;
        this.data = data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public String getName() {
        return eventName;
    }

    @Override
    public EventHandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static EventHandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
