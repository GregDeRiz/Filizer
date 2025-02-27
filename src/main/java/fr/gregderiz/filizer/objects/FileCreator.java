package fr.gregderiz.filizer.objects;

import fr.gregderiz.filizer.managers.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCreator {
    private final FileManager fileManager;

    public FileCreator(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Nullable
    public File createDirectory(File parent, String name) {
        File file = new File(parent, name);
        if (!parent.exists()) if (!parent.mkdir()) return null;
        if (!file.exists()) if (!file.mkdir()) return null;
        if (!Files.isDirectory(file.toPath())) return null;

        this.fileManager.addFile(file, true);
        return file;
    }

    @Nullable
    public File createFile(FileBuilder fileBuilder) {
        File file = fileBuilder.save();
        this.fileManager.addFile(file, false);
        return file;
    }

    @Nullable
    public File createFile(File parent, String name) {
        File file = new File(parent, name + ".yml");
        if (!parent.exists()) if (!parent.mkdir()) return null;

        try {
            if (!file.createNewFile()) return null;

            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.fileManager.addFile(file, false);
        return file;
    }
}
