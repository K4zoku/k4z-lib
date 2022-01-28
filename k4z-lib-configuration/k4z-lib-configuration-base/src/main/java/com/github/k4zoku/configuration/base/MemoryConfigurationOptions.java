package com.github.k4zoku.configuration.base;

public class MemoryConfigurationOptions extends ConfigurationOptions {

    public MemoryConfigurationOptions(MemoryConfiguration configuration, char pathSeparator) {
        super(configuration, pathSeparator);
    }

    public MemoryConfigurationOptions(MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public MemoryConfiguration configuration() {
        return (MemoryConfiguration) this.configuration;
    }
}
