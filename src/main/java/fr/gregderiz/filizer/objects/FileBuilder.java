package fr.gregderiz.filizer.objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileBuilder {
    private final File file;
    private FileConfiguration fileConfiguration;

    public FileBuilder(File parent, String name) {
        this.file = new File(parent, name + ".yml");
        if (!parent.exists()) if (!parent.mkdir()) return;

        try {
            if (!this.file.createNewFile()) return;

            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileBuilder set(String name, Object value) {
        this.fileConfiguration.set(name, value);
        return this;
    }

    public FileBuilder list(String name, List<Object> values) {
        this.fileConfiguration.set(name, values);
        return this;
    }

    public FileBuilder section(String name, Map<String, Object> values) {
        this.fileConfiguration.createSection(name, values);
        return this;
    }

    public File save() {
        try {
            this.fileConfiguration.save(this.file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
