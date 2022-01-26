package com.github.k4zoku.lib.event;

public interface CancellableEvent extends Event {
    boolean isCancelled();
    void setCancelled(boolean cancel);
}
