package com.github.k4zoku.configuration.base;

public interface ConfigurationOptions {
    default char pathSeparator() {
        return '.';
    }

}
