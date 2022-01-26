import com.github.k4zoku.lib.event.CancellableEvent;
import com.github.k4zoku.lib.event.HandlerList;

public class SimpleDataEvent implements CancellableEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String data;

    public SimpleDataEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
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
