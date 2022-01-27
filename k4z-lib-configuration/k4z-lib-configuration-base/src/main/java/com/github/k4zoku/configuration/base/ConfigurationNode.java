package com.github.k4zoku.configuration.base;

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
     * @return true if the node has child node
     */
    boolean hasChild();

    /**
     * Get all child node
     * <br>
     * If recursive is true, it will return all child node includes their child
     * <br>
     * <b>Note:</b> If the node has no child section, return empty map
     *
     * @param recursive whether to get all child sections recursively
     * @return all child sections in a map with section name as key
     */
    @NotNull
    Map<String, ConfigurationNode> getChild(boolean recursive);

    /**
     * Get all child keys of current node
     *
     * @param recursive whether to get all child keys recursively
     * @return all child keys in a set
     */
    @NotNull
    Set<String> getKeys(boolean recursive);

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
