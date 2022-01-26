package com.github.k4zoku.lib.event;

public class RegisteredListener {
    private final Listener listener;
    private final EventPriority priority;
    private final EventExecutor executor;
    private final boolean ignoreCancelled;

    public RegisteredListener(Listener listener, EventPriority priority, EventExecutor executor, boolean ignoreCancelled) {
        this.listener = listener;
        this.priority = priority;
        this.executor = executor;
        this.ignoreCancelled = ignoreCancelled;
    }

    public Listener getListener() {
        return this.listener;
    }

    public EventPriority getPriority() {
        return this.priority;
    }

    public void call(final Event event) throws EventException {
        if (event instanceof CancellableEvent && ((CancellableEvent) event).isCancelled()) {
            return;
        }
        this.executor.execute(this.listener, event);
    }

    public boolean isIgnoringCancelled() {
        return this.ignoreCancelled;
    }

}
