package com.github.k4zoku.configuration.base.file;

import com.github.k4zoku.configuration.base.MemoryConfiguration;
import com.github.k4zoku.configuration.base.exception.InvalidConfigurationException;

import java.io.*;

public abstract class FileConfiguration extends MemoryConfiguration {

    protected FileConfiguration() {
        super();
    }

    public void load(String file) throws IOException, InvalidConfigurationException {
        load(new File(file));
    }

    public void load(File file) throws IOException, InvalidConfigurationException {
        load(new FileInputStream(file));
    }

    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        load(new InputStreamReader(stream));
    }

    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        try (BufferedReader input = new BufferedReader(reader)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            this.loadFromString(builder.toString());
        }
    }

    public void save(String file) throws IOException {
        save(new File(file));
    }

    public void save(File file) throws IOException {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), options().charset())) {
            writer.write(saveToString());
        }
    }

    public abstract String saveToString();

    public abstract void loadFromString(String contents) throws InvalidConfigurationException;

    @Override
    public FileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions) this.options;
    }
}
