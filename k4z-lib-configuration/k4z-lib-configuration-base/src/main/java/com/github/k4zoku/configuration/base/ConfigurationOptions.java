package com.github.k4zoku.configuration.base;

public abstract class ConfigurationOptions {

    protected final Configuration configuration;
    protected char pathSeparator = '.';

    protected ConfigurationOptions(Configuration configuration, char pathSeparator) {
        this.configuration = configuration;
        this.pathSeparator = pathSeparator;
    }

    protected ConfigurationOptions(Configuration configuration) {
        this.configuration = configuration;
    }

    public char pathSeparator() {
        return this.pathSeparator;
    }

    public ConfigurationOptions pathSeparator(char separator) {
        this.pathSeparator = separator;
        return this;
    }

    public Configuration configuration() {
        return this.configuration;
    }
}
