package com.github.k4zoku.lib.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class SimpleEventManager implements EventManager {

    private final Map<Class<? extends Event>, Set<EventHandlerList>> handlerListMap = new HashMap<>();

    @Override
    public <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback) {
        return register(eventType, callback, EventPriority.NORMAL);
    }

    @Override
    public <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority) {
        return register(eventType, callback, priority, true);
    }

    @Override
    public <T extends Event> EventListener register(Class<T> eventType, Consumer<T> callback, EventPriority priority, boolean ignoreCancelled) {
        // create dummy listener
        EventListener listener = new EventListener() {};
        EventHandlerList handlers = getEventListeners(eventType);
        addHandlers(eventType, handlers);
        EventExecutor executor = (l, e) -> callback.accept(eventType.cast(e));
        handlers.register(new RegisteredEventListener(listener, priority, executor, ignoreCancelled));
        return listener;
    }

    @Override
    public void registerAll(EventListener listener) {
        Map<Class<? extends Event>, Set<RegisteredEventListener>> listeners =
                createRegisteredListeners(listener);
        listeners.forEach((eventClass, registeredListeners) -> {
            Class<? extends Event> registrationClass = getRegistrationClass(eventClass);
            EventHandlerList handlers = getEventListeners(registrationClass);
            addHandlers(eventClass, handlers);
            handlers.registerAll(registeredListeners);
        });
    }

    @Override
    public void unregister(EventListener listener) {
        handlerListMap.values()
                .forEach(handlers -> handlers.forEach(handler -> handler.unregister(listener)));
    }

    @Override
    public void unregisterAll(Class<? extends Event> eventType) {
        Set<EventHandlerList> handlers = handlerListMap.computeIfAbsent(eventType, k -> new HashSet<>());
        handlers.forEach(EventHandlerList::unregisterAll);
        handlers.clear();
    }

    @Override
    public void unregisterAll() {
        handlerListMap.values()
                .forEach(handlers -> handlers.forEach(EventHandlerList::unregisterAll));
        handlerListMap.clear();
    }

    @Override
    public void fire(Event event) {
        EventHandlerList handlers = event.getHandlers();
        RegisteredEventListener[] listeners = handlers.getRegisteredListeners();

        for (RegisteredEventListener listener : listeners) {
            try {
                listener.call(event);
            } catch (EventException e) {
                System.err.println("Could not pass event " + event.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addHandlers(Class<? extends Event> eventType, EventHandlerList handlers) {
        handlerListMap.computeIfAbsent(eventType, k -> new HashSet<>()).add(handlers);
    }

    private EventHandlerList getEventListeners(Class<? extends Event> eventClass) {
        try {
            Method method = eventClass.getMethod("getHandlerList");
            return (EventHandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to get event handler list for event " + eventClass.getName(), e);
        }
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

    private Map<Class<? extends Event>, Set<RegisteredEventListener>> createRegisteredListeners(EventListener listener) {
        Map<Class<? extends Event>, Set<RegisteredEventListener>> registeredListeners = new HashMap<>();
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
            Set<RegisteredEventListener> eventSet = registeredListeners
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
            eventSet.add(new RegisteredEventListener(listener, priority, executor, ignoreCancelled));
        }
        return registeredListeners;
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
}
