import com.github.k4zoku.lib.event.EventHandler;
import com.github.k4zoku.lib.event.EventListener;
import com.github.k4zoku.lib.event.EventManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleEventTest {


    @Test
    void testEvent() {
        EventManager eventManager = new EventManager();
        AtomicReference<String> received = new AtomicReference<>();
        EventListener listener = new EventListener() {
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
