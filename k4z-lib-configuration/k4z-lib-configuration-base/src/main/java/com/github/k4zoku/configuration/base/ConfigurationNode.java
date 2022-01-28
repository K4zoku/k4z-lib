package com.github.k4zoku.configuration.base;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Represents a node in a {@link Configuration} tree.
 *
 * @author K4zoku
 */
public interface ConfigurationNode {

    /**
     * Determine whether the node is root node
     *
     * @return true if the node is root node
     */
    boolean isRoot();

    /**
     * Get root node, which is the root node of the whole configuration
     *
     * @return root node, itself if the node is root node
     */
    ConfigurationNode getRoot();

    /**
     * Determine whether the node has parent node
     *
     * @return true if the node has parent node
     */
    boolean hasParent();

    /**
     * Get the parent node
     *
     * @return parent node, null if the node has no parent
     */
    @Nullable
    ConfigurationNode getParent();

    /**
     * Determine whether the node has child node
     *
     * @param key child node key
     * @return true if the node has child node
     */
    boolean hasChild(String key);

    /**
     * Get child node with specified key
     * <br>
     * default to {@link #getChild(String, boolean) getChild(key, false)}
     *
     * @param key child node key
     * @return child node, null if no child node with specified key exists
     */
    default ConfigurationNode getChild(String key) {
        return getChild(key, false);
    }

    /**
     * Get child node with specified key
     *
     * @param key child node key
     * @param create whether to create child node if no child node with specified key exists
     * @return child node, nullable if create is false
     */
    @Contract("_, true -> !null")
    ConfigurationNode getChild(String key, boolean create);

    /**
     * Create a new child node with specified key
     *
     * @param key child node key
     * @return new child node
     */
    ConfigurationNode createChild(String key);

    /**
     * Determine whether the node has children
     *
     * @return true if the node has children
     */
    boolean hasChildren();

    /**
     * Get all children of current node as a map with key as key identifier and
     * value as child node
     * <br>
     * If recursive is true, it will return all children includes their child
     * <br>
     * <b>Note:</b> If the node has no child section, return empty map
     *
     * @param recursive whether to get all child sections recursively
     * @return all child sections in a map with section name as key
     */
    @NotNull
    Map<String, ConfigurationNode> getChildrenMap(boolean recursive);

    /**
     * Get all children of current node as a set
     * <br>
     * If recursive is true, it will return all children includes their child
     * <br>
     * <b>Note:</b> If the node has no child section, return empty set
     *
     * @param recursive whether to get all child keys recursively
     * @return all children in a set
     */
    @NotNull
    Set<ConfigurationNode> getChildren(boolean recursive);

    /**
     * Get all children keys
     *
     * @param recursive whether to get all child keys recursively
     * @return all children key
     */
    @NotNull
    Set<String> getKeys(boolean recursive);

    /**
     * Get all children values
     *
     * @param recursive whether to get all child values recursively
     * @return all children value
     */
    Set<Object> getValues(boolean recursive);

    /**
     * Get the key identifier of current node
     *
     * @return the key
     */
    @NotNull
    String key();

    /**
     * Get value of current node
     *
     * @return value of current node, null if the node has no value
     */
    @Nullable
    Object value();

    /**
     * Set value of current node
     *
     * @param value value to set, null to remove value
     */
    void setValue(@Nullable Object value);

}
