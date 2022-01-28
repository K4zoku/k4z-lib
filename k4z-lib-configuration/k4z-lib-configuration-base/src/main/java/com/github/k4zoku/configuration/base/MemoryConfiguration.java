package com.github.k4zoku.configuration.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MemoryConfiguration extends MemoryConfigurationNode implements Configuration {

    protected MemoryConfiguration(ConfigurationNode root, ConfigurationNode parent, String key) {
        super(root, parent, key);
    }

    public MemoryConfiguration() {
        super();
    }

    @Override
    public ConfigurationNode createChild(String key) {
        return this.children.computeIfAbsent(key, k -> new MemoryConfiguration(getRoot(), this, k));
    }

    public static String createPath(final @Nullable ConfigurationNode node, final char separator, final ConfigurationNode relativeTo) {
        if (node == null) {
            return "";
        }
        StringBuilder path = new StringBuilder(node.key());
        for (ConfigurationNode parent = node.getParent(); parent != null && parent != relativeTo; parent = parent.getParent()) {
            if (path.length() >= node.key().length()) {
                path.insert(0, separator);
            }
            path.insert(0, parent.key());
        }
        return path.toString();
    }

    @Override
    public @NotNull Map<String, ConfigurationNode> getChildrenMap(boolean recursive) {
        Map<String, ConfigurationNode> result = new LinkedHashMap<>();
        if (recursive) {
            Map<ConfigurationNode, String> paths = new HashMap<>();
            Queue<ConfigurationNode> queue = new LinkedList<>(this.children.values());
            final char separator = options().pathSeparator();
            while (!queue.isEmpty()) {
                ConfigurationNode node = queue.poll();
                String path = paths.computeIfAbsent(
                    node.getParent(),
                    parent -> createPath(parent, separator, this)
                );
                if (path.length() > 0) {
                    path += separator;
                }
                path += node.key();
                result.put(path, node);
                if (node.hasChildren()) {
                    queue.addAll(node.getChildren(false));
                }

            }
        } else {
            result.putAll(this.children);
        }
        return result;
    }

    @Override
    public Object get(String path) {
        return get(path, null);
    }

    @Override
    public Object get(String path, Object def) {
        ConfigurationNode node = getNode(path, false);
        return node == null ? def : node.value();
    }

    protected ConfigurationNode getNode(String path, boolean create) {
        ConfigurationNode node = this;
        final char separator = options().pathSeparator();
        int i1 = -1;
        int i2;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String part = path.substring(i2, i1);
            node = node.getChild(part, create);
            if (node == null) {
                return null;
            }
        }
        node = node.getChild(path.substring(i2), create);
        return node;
    }

    @Override
    public void set(String path, Object value) {
        getNode(path, true).setValue(value);
    }

    @Override
    public ConfigurationOptions options() {
        return new ConfigurationOptions() {
            @Override
            public char pathSeparator() {
                return ConfigurationOptions.super.pathSeparator();
            }
        };
    }

}
