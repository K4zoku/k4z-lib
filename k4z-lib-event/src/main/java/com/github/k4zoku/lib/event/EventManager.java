package com.github.k4zoku.lib.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

    public EventManager() {
        // EMPTY
    }

    public void callEvent(Event event) {
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();

        for (RegisteredListener listener : listeners) {
            try {
                listener.call(event);
            } catch (Exception e) {
                System.err.println("Could not pass event " + event.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void registerEvents(Listener listener) {
        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : createRegisteredListeners(listener).entrySet()) {
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }
    }

    private Class<? extends Event> getEventHandlerType(Method method) {
        if (
                // check if the method is an event handler
                method.isAnnotationPresent(EventHandler.class) &&
                // check if the method is not bridge and not synthetic
                !method.isBridge() && !method.isSynthetic() &&
                // check if the method parameters count are correct
                method.getParameterCount() == 1
        ) {
            // check if the method parameter is an event
            final Class<?> checkClass = method.getParameterTypes()[0];
            if (Event.class.isAssignableFrom(checkClass)) {
                return checkClass.asSubclass(Event.class);
            }
        }
        return null;
    }

    private Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener) {
        Map<Class<? extends Event>, Set<RegisteredListener>> registeredListeners = new HashMap<>();
        Set<Method> methods;
        Class<?> listenerClass = listener.getClass();
        try {
            Method[] publicMethods = listenerClass.getMethods();
            Method[] declaredMethods = listenerClass.getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + declaredMethods.length, 1.0f);
            methods.addAll(Arrays.asList(publicMethods));
            methods.addAll(Arrays.asList(declaredMethods));
        } catch (NoClassDefFoundError e) {
            return registeredListeners;
        }
        for (final Method method : methods) {
            Class<? extends Event> eventClass = getEventHandlerType(method);
            if (eventClass == null) {
                continue;
            }
            EventHandler eh = method.getAnnotation(EventHandler.class);
            Set<RegisteredListener> eventSet = registeredListeners
                    .computeIfAbsent(eventClass, unused -> new HashSet<>());
            EventPriority priority = eh.priority();
            EventExecutor executor = (l, e) -> {
                try {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(l, e);
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new EventException(ex, "Cannot access method " + method.getName() + " in " + method.getDeclaringClass().getCanonicalName());
                } catch (Exception ex) {
                    throw new EventException(ex, "Unhandled exception in method " + method.getName() + " in " + method.getDeclaringClass().getCanonicalName());
                }
            };
            boolean ignoreCancelled = eh.ignoreCancelled();
            eventSet.add(new RegisteredListener(listener, priority, executor, ignoreCancelled));
        }
        return registeredListeners;
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalStateException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    public void registerEvent(Class<? extends Event> eventClass, Listener listener, EventPriority priority, EventExecutor executor) {
        registerEvent(eventClass, listener, priority, executor, false);
    }

    public void registerEvent(Class<? extends Event> eventClass, Listener listener, EventPriority priority, EventExecutor executor, boolean ignoreCancelled) {
        getEventListeners(eventClass)
                .register(new RegisteredListener(listener, priority, executor, ignoreCancelled));
    }

    private HandlerList getEventListeners(Class<? extends Event> eventClass) {
        try {
            Method method = eventClass.getMethod("getHandlerList");
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to get event handler list for event " + eventClass.getName(), e);
        }
    }

}
