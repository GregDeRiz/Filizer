package fr.gregderiz.filizer.objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileEditor {
    private final File file;
    private final FileConfiguration fileConfiguration;

    public FileEditor(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    private String search(String name) {
        for (String key : this.fileConfiguration.getKeys(true)) {
            if (key.contains(name)) return key;
        }
        return name;
    }

    public FileEditor edit(String name, Object value) {
        this.fileConfiguration.set(search(name), value);
        return this;
    }

    public FileEditor delete(String name) {
        this.fileConfiguration.set(search(name), null);
        return this;
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(String name) {
        return this.fileConfiguration.get(search(name));
    }

    public List<?> list(String name) {
        return this.fileConfiguration.getList(search(name));
    }

    public Set<String> section(String name) {
        String searchedName = search(name);
        if (!this.fileConfiguration.isConfigurationSection(searchedName)) return null;

        ConfigurationSection section = this.fileConfiguration.getConfigurationSection(searchedName);
        if (section == null) return null;

        return section.getKeys(false);
    }
}
