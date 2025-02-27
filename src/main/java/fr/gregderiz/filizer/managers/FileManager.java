package fr.gregderiz.filizer.managers;

import com.google.common.collect.Sets;
import fr.gregderiz.filizer.controllers.FileController;
import fr.gregderiz.filizer.controllers.FolderController;
import fr.gregderiz.filizer.objects.FileCreator;
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
    private final FileCreator fileCreator;

    public FileManager(Plugin plugin) {
        this.files = Sets.newHashSet();
        this.folderController = new FolderController(this);
        this.fileController = new FileController(this);
        this.fileCreator = new FileCreator(this);
        addFile(plugin.getDataFolder(), true);
    }

    public void addFile(File file, boolean searchFilesInFolders) {
        if (file == null) return;
        if (!Files.isDirectory(file.toPath())) {
            this.files.add(file);
            return;
        }

        for (File child : this.folderController.getFolderListFiles(file)) {
            if (Files.isDirectory(child.toPath()) && searchFilesInFolders) {
                this.folderController.loopFolder(child);
                continue;
            }
            if (!Files.isDirectory(child.toPath())) this.files.add(child);
        }
    }

    public void removeFile(File file, boolean canBeDeleted) {
        if (file == null) return;
        if (Files.isDirectory(file.toPath())) {
            Set<File> fileList = this.folderController.getFolderListFiles(file);
            if (!fileList.isEmpty()) {
                fileList.stream().filter(this.files::contains).forEach(this.files::remove);
                return;
            }
        }

        this.files.remove(file);
        if (canBeDeleted) deleteFile(file);
    }

    public void deleteFile(File file) {
        if (file == null) return;
        if (Files.isDirectory(file.toPath())) {
            Set<File> fileList = this.folderController.getFolderListFiles(file);
            fileList.forEach(this::deleteFile);
        }
        if (file.delete()) return;

        Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                .deserialize("The file " + file.getName() + " was not correctly deleted."));
    }

    public void destroy() {
        this.files.clear();
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

    public FileCreator getFileCreator() {
        return this.fileCreator;
    }
}
