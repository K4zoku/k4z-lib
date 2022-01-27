package com.github.k4zoku.lib.event;

import java.util.*;

public class EventHandlerList {

    private final EnumMap<EventPriority, ArrayList<RegisteredEventListener>> handlerSlots;
    private RegisteredEventListener[] handlers = null;

    public EventHandlerList() {
        handlerSlots = new EnumMap<>(EventPriority.class);
        for (EventPriority priority : EventPriority.values()) {
            handlerSlots.put(priority, new ArrayList<>());
        }
    }

    public synchronized void register(RegisteredEventListener listener) {
        if (handlerSlots.get(listener.getPriority()).contains(listener))
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        handlers = null;
        handlerSlots.get(listener.getPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredEventListener> listeners) {
        for (RegisteredEventListener listener : listeners) {
            register(listener);
        }
    }

    public synchronized void unregister(RegisteredEventListener listener) {
        if (handlerSlots.get(listener.getPriority()).remove(listener)) {
            handlers = null;
        }
    }

    public synchronized void unregister(EventListener listener) {
        boolean changed = false;
        for (List<RegisteredEventListener> list : handlerSlots.values()) {
            for (ListIterator<RegisteredEventListener> i = list.listIterator(); i.hasNext();) {
                if (i.next().getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) handlers = null;
    }

    public synchronized void unregisterAll() {
        handlerSlots.clear();
        handlers = null;
    }

    public synchronized void bake() {
        if (handlers != null) return; // don't re-bake when still valid
        List<RegisteredEventListener> entries = new ArrayList<>();
        for (Map.Entry<EventPriority, ArrayList<RegisteredEventListener>> entry : handlerSlots.entrySet()) {
            entries.addAll(entry.getValue());
        }
        handlers = entries.toArray(new RegisteredEventListener[0]);
    }

    public RegisteredEventListener[] getRegisteredListeners() {
        RegisteredEventListener[] handlers;
        while ((handlers = this.handlers) == null) bake(); // This prevents fringe cases of returning null
        return handlers;
    }
}
