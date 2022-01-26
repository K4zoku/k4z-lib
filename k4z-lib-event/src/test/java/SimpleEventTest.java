import com.github.k4zoku.lib.event.EventHandler;
import com.github.k4zoku.lib.event.EventManager;
import com.github.k4zoku.lib.event.Listener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleEventTest {


    @Test
    void testEvent() {
        EventManager eventManager = new EventManager();
        AtomicReference<String> received = new AtomicReference<>();
        Listener listener = new Listener() {
            @EventHandler
            public void onEvent(SimpleDataEvent event) {
                received.set(event.getData());
            }
        };
        eventManager.registerEvents(listener);
        eventManager.callEvent(new SimpleDataEvent("Hello World!"));
        assertEquals("Hello World!", received.get());
    }
}
