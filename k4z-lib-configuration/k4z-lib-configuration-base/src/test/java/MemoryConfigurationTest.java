import com.github.k4zoku.configuration.base.MemoryConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MemoryConfigurationTest {
    @Test
    void testConfiguration() {
        MemoryConfiguration configuration = new MemoryConfiguration();
        configuration.set("key", "value");
        configuration.set("key2", "value2");
        configuration.set("key.child", "value");
        configuration.set("key.child.child2", "value");
        configuration.set("key.child.child2.child3", "???");
        assertEquals("value", configuration.get("key"));
        assertEquals("value2", configuration.get("key2"));
        assertEquals("value", configuration.get("key.child"));
        assertEquals("value", configuration.get("key.child.child2"));
        assertEquals("???", configuration.get("key.child.child2.child3"));
        assertNull(configuration.get("key.child.child2.child3.child4"));
        assertEquals(2, configuration.getChildren(false).size());
        assertEquals(5, configuration.getChildren(true).size());
        assertEquals(2, configuration.getChildrenMap(false).size());
        assertEquals(5, configuration.getChildrenMap(true).size());
    }
}
