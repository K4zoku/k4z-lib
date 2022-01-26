package com.github.k4zoku.lib.event;

import java.util.*;

public class HandlerList {

    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerSlots;
    private RegisteredListener[] handlers = null;

    public HandlerList() {
        handlerSlots = new EnumMap<>(EventPriority.class);
        for (EventPriority priority : EventPriority.values()) {
            handlerSlots.put(priority, new ArrayList<>());
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if (handlerSlots.get(listener.getPriority()).contains(listener))
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        handlers = null;
        handlerSlots.get(listener.getPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            register(listener);
        }
    }

    public synchronized void unregister(RegisteredListener listener) {
        if (handlerSlots.get(listener.getPriority()).remove(listener)) {
            handlers = null;
        }
    }

    public synchronized void unregister(Listener listener) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerSlots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
                if (i.next().getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) handlers = null;
    }

    public synchronized void bake() {
        if (handlers != null) return; // don't re-bake when still valid
        List<RegisteredListener> entries = new ArrayList<>();
        for (Map.Entry<EventPriority, ArrayList<RegisteredListener>> entry : handlerSlots.entrySet()) {
            entries.addAll(entry.getValue());
        }
        handlers = entries.toArray(new RegisteredListener[0]);
    }

    public RegisteredListener[] getRegisteredListeners() {
        RegisteredListener[] handlers;
        while ((handlers = this.handlers) == null) bake(); // This prevents fringe cases of returning null
        return handlers;
    }
}
