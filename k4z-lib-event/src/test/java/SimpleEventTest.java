import com.github.k4zoku.lib.event.EventHandler;
import com.github.k4zoku.lib.event.EventListener;
import com.github.k4zoku.lib.event.EventManager;
import com.github.k4zoku.lib.event.SimpleEventManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleEventTest {

    @Test
    void testEventWithListener() {
        EventManager eventManager = new SimpleEventManager();
        AtomicReference<String> received = new AtomicReference<>();
        EventListener listener = new EventListener() {
            @EventHandler
            public void onEvent(SimpleDataEvent event) {
                received.set(event.getData());
            }
        };
        eventManager.registerAll(listener);
        eventManager.fire(new SimpleDataEvent("Hello World!"));
        assertEquals("Hello World!", received.get());
    }

    @Test
    void testEventWithoutListener() {
        EventManager eventManager = new SimpleEventManager();
        AtomicReference<String> received = new AtomicReference<>();
        eventManager.register(SimpleDataEvent.class, ((event) -> received.set(event.getData())));
        eventManager.fire(new SimpleDataEvent("Hello World!"));
        assertEquals("Hello World!", received.get());
    }

    @Test
    void testUnregister() {
        EventManager eventManager = new SimpleEventManager();
        AtomicReference<String> received = new AtomicReference<>();
        EventListener listener = new EventListener() {
            @EventHandler
            public void onEvent(SimpleDataEvent event) {
                received.set(event.getData());
            }
        };
        eventManager.registerAll(listener);
        eventManager.unregister(listener);
        eventManager.fire(new SimpleDataEvent("Hello World!"));
        assertNull(received.get());
    }

    @Test
    void testUnregisterWithoutListener() {
        EventManager eventManager = new SimpleEventManager();
        AtomicReference<String> received = new AtomicReference<>();
        EventListener dummy = eventManager.register(SimpleDataEvent.class, ((event) -> received.set(event.getData())));
        eventManager.unregister(dummy);
        eventManager.fire(new SimpleDataEvent("Hello World!"));
        assertNull(received.get());
    }

    @Test
    void testUnregisterAllType() {
        EventManager eventManager = new SimpleEventManager();
        AtomicReference<String> received = new AtomicReference<>();
        eventManager.register(SimpleDataEvent.class, ((event) -> received.set(event.getData())));
        eventManager.unregisterAll(SimpleDataEvent.class);
        eventManager.fire(new SimpleDataEvent("Hello World!"));
        assertNull(received.get());
    }

}
