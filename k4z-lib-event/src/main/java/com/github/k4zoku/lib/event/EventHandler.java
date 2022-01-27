package com.github.k4zoku.lib.event;

import java.lang.annotation.*;

/**
 * Annotate a method to be called when an event is fired.
 * <br>
 * The method must have the following signature:
 * <br>
 * <code>void onEvent(Event event)</code>
 * <br>
 * <li><code>Event</code> is the event type.</li>
 * <li><code>event</code> is the event that was fired.</li>
 * <br>
 * <br>
 * <b>Example:</b>
 * <br>
 * <code>@EventHandler<br>public void onEvent(Event event) {<br> // do something<br>}</code>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    /**
     * The priority of the event handler.
     * @return the priority.
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Call the event handler when the event is fired even if the event is cancelled.
     *
     * @return true if the event handler should be called even if the event is cancelled.
     */
    boolean ignoreCancelled() default false;
}
