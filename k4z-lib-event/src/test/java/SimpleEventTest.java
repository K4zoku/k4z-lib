import com.github.k4zoku.lib.event.EventHandler;
import com.github.k4zoku.lib.event.EventManager;
import com.github.k4zoku.lib.event.Listener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleEventTest {

    @Test
    void testEvent() {
        String orig = "Hello World!";
        AtomicReference<String> received = new AtomicReference<>();
        EventManager eventManager = new EventManager();
        Listener listener = new Listener() {
            @EventHandler
            public void onEvent(SimpleDataEvent event) {
                received.set(event.getData());
            }
        };
        eventManager.registerEvents(listener);
        eventManager.callEvent(new SimpleDataEvent(orig));
        assertEquals(orig, received.get());
    }
}
