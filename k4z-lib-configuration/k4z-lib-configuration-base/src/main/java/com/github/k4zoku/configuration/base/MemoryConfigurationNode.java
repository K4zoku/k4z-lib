package com.github.k4zoku.configuration.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MemoryConfigurationNode implements ConfigurationNode {

    protected final ConfigurationNode root;
    protected final ConfigurationNode parent;
    protected final String key;
    protected Object value;
    protected final Map<String, ConfigurationNode> children;

    protected MemoryConfigurationNode(@Nullable ConfigurationNode root, @Nullable ConfigurationNode parent, @NotNull String key, @Nullable Object value, @Nullable Map<String, ConfigurationNode> children) {
        this.root = root;
        this.parent = parent;
        this.key = key;
        this.value = value;
        this.children = children == null ? new LinkedHashMap<>() : children;
    }

    protected MemoryConfigurationNode(@Nullable ConfigurationNode root, @Nullable ConfigurationNode parent, @NotNull String key) {
        this(root, parent, key, null, null);
    }

    protected MemoryConfigurationNode() {
        this(null, null, "");
    }

    @Override
    public boolean isRoot() {
        return this.root == null;
    }

    @Override
    public ConfigurationNode getRoot() {
        return isRoot() ? this : this.root;
    }

    @Override
    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public @Nullable ConfigurationNode getParent() {
        return this.parent;
    }

    @Override
    public boolean hasChild(String key) {
        return children.containsKey(key);
    }

    @Override
    public ConfigurationNode getChild(String key, boolean create) {
        if (create && !hasChild(key)) {
            this.children.put(key, createChild(key));
        }
        return this.children.get(key);
    }

    @Override
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override
    public @NotNull Set<ConfigurationNode> getChildren(boolean recursive) {
        Set<ConfigurationNode> result = new HashSet<>();
        if (recursive) {
            Queue<ConfigurationNode> queue = new LinkedList<>();
            queue.add(this);
            while (!queue.isEmpty()) {
                ConfigurationNode node = queue.poll();
                if (node.hasChildren()) {
                    Set<ConfigurationNode> children = node.getChildren(false);
                    for (ConfigurationNode child : children) {
                        result.add(child);
                        if (child.hasChildren()) {
                            queue.add(child);
                        }
                    }
                }
            }
        } else {
            result.addAll(this.children.values());
        }
        return result;
    }

    @Override
    public Set<Object> getValues(boolean recursive) {
        Set<Object> result = new HashSet<>();
        if (recursive) {
            Queue<ConfigurationNode> queue = new LinkedList<>();
            queue.add(this);
            while (!queue.isEmpty()) {
                ConfigurationNode node = queue.poll();
                if (node.hasChildren()) {
                    Set<ConfigurationNode> children = node.getChildren(false);
                    for (ConfigurationNode child : children) {
                        result.add(child.value());
                        if (child.hasChildren()) {
                            queue.add(child);
                        }
                    }
                }
            }
        } else {
            result.addAll(this.children.values().stream().map(ConfigurationNode::value).collect(Collectors.toSet()));
        }
        return result;
    }

    @Override
    @NotNull
    public String key() {
        return this.key;
    }

    @Override
    public @Nullable Object value() {
        return this.value;
    }

    @Override
    public void setValue(@Nullable Object value) {
        this.value = value;
    }

}
