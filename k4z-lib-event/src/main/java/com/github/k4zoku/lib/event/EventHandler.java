package com.github.k4zoku.lib.event;

import java.lang.annotation.*;

/**
 * Annotate a method to be called when an event is fired.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority priority() default EventPriority.NORMAL;
    boolean ignoreCancelled() default false;
}
