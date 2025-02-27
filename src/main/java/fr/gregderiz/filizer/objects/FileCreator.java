package fr.gregderiz.filizer;

import fr.gregderiz.filizer.managers.FileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
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
        if (!parent.exists()) {
            if (!parent.mkdir()) {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                        .deserialize("The parent directory was not correctly created."));
                return null;
            }
        }
        if (file.exists()) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("That directory already exist."));
            return null;
        }
        if (!file.mkdir()) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("The directory was not correctly created."));
            return null;
        }
        if (!Files.isDirectory(file.toPath())) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                    .deserialize("The file is not a directory."));
            if (!file.delete()) {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                        .deserialize("The directory was not correctly deleted."));
            }
        }

        this.fileManager.addFile(file, true);
        return file;
    }

    @Nullable
    public File createFile(File parent, String name) {
        File file = new File(parent, name + ".yml");
        if (!parent.exists()) {
            if (!parent.mkdir()) {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                        .deserialize("The parent directory was not correctly created."));
                return null;
            }
        }
        if (file.exists()) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("That file already exist."));
            return null;
        }

        try {
            if (!file.createNewFile()) {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("The File was not correctly created."));
                return null;
            }

            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.fileManager.addFile(file, false);
        return file;
    }
}
