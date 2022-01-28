package com.github.k4zoku.configuration.base.file;

import com.github.k4zoku.configuration.base.MemoryConfigurationOptions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileConfigurationOptions extends MemoryConfigurationOptions {

    private Charset charset = StandardCharsets.UTF_8;

    public FileConfigurationOptions(FileConfiguration configuration) {
        super(configuration);
    }

    public Charset charset() {
        return this.charset;
    }

    public FileConfigurationOptions charset(Charset charset) {
        this.charset = charset;
        return this;
    }
}
