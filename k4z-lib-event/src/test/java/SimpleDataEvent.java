import com.github.k4zoku.lib.event.CancellableEvent;
import com.github.k4zoku.lib.event.EventHandlerList;

public class SimpleDataEvent implements CancellableEvent {

    private static final EventHandlerList HANDLER_LIST = new EventHandlerList();

    private final String data;

    public SimpleDataEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public EventHandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static EventHandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
