package com.github.k4zoku.configuration.base;

public interface Configuration {

    /**
     * Get options for the configuration.
     *
     * @return the options
     */
    ConfigurationOptions options();

    /**
     * Get the configuration node at the given path.
     *
     * @param path the path
     * @return the configuration node
     */
    ConfigurationNode getNode(String path);

    /**
     * Get value of the configuration node at the given path.
     *
     * @param path the path
     * @return the value
     */
    Object get(String path);

    /**
     * Get value of the configuration node at the given path.
     *
     * @param path the path
     * @param def the default value
     * @return the value, or the default value if the node does not exist
     */
    Object get(String path, Object def);

    /**
     * Set value of the configuration node at the given path.
     *
     * @param path the path
     * @param value the value
     */
    void set(String path, Object value);

}
