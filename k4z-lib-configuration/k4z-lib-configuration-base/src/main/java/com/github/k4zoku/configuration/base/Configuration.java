package com.github.k4zoku.configuration.base;

public interface Configuration {
    ConfigurationOptions options();
    Object get(String path);
    Object get(String path, Object def);
    void set(String path, Object value);
}
