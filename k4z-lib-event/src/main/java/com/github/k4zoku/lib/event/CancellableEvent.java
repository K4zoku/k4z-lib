package com.github.k4zoku.lib.event;

public interface CancellableEvent extends Event {

    /**
     * Determines whether the event is cancelled.
     *
     * @return true if the event is cancelled
     */
    boolean isCancelled();

    /**
     * Sets whether the event is cancelled.
     *
     * @param cancel true if the event is cancelled
     */
    void setCancelled(boolean cancel);

}
