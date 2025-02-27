package fr.gregderiz.filizer;

import com.google.common.collect.Sets;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.Files;
import java.util.Set;

public final class FileManager {
    private final Set<File> files;
    private final FolderController folderController;
    private final FileController fileController;

    public FileManager(Plugin plugin) {
        this.files = Sets.newHashSet();
        this.folderController = new FolderController(this);
        this.fileController = new FileController(this.files, this.folderController);
        addFile(plugin.getDataFolder(), true);
    }

    public void addFile(File file, boolean searchFilesInFolders) {
        if (file == null) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                    .deserialize("The File you want to add is null."));
            return;
        }

        if (!Files.isDirectory(file.toPath())) {
            this.files.add(file);
            return;
        }

        for (File value : this.folderController.getFolderListFiles(file)) {
            if (Files.isDirectory(value.toPath()) && searchFilesInFolders) {
                this.folderController.loopFolder(value);
                continue;
            }

            if (!Files.isDirectory(value.toPath())) this.files.add(value);
        }
    }

    public void removeFile(File file) {
        if (file == null) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                    .deserialize("The File you want to add is null."));
            return;
        }

        if (Files.isDirectory(file.toPath())) {
            Set<File> fileList = this.folderController.getFolderListFiles(file);
            if (!fileList.isEmpty()) {
                fileList.stream().filter(this.files::contains).forEach(this.files::remove);
                return;
            }
        }

        this.files.remove(file);
    }

    public Set<File> getFiles() {
        return this.files;
    }

    public FolderController getFolderController() {
        return this.folderController;
    }

    public FileController getFileController() {
        return this.fileController;
    }
}
