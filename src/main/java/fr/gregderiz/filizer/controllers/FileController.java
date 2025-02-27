package fr.gregderiz.filizer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

public final class FileController {
    private final Set<File> files;
    private final FolderController folderController;

    public FileController(Set<File> files, FolderController folderController) {
        this.folderController = folderController;
        this.files = files;
    }

    public Optional<File> findFileByName(String folderName, String name) {
        Optional<File> optionalFolder = this.folderController.findFolderByName(folderName);
        if (optionalFolder.isEmpty()) return Optional.empty();

        Set<File> fileList = this.folderController.getFolderListFiles(optionalFolder.get());
        if (files.isEmpty()) return Optional.empty();

        return fileList.stream().filter(file -> file.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<File> findFileByName(File folder, String name) {
        for (File file : this.files) {
            try {
                if (!Files.isSameFile(file.getParentFile().toPath(), folder.toPath())) {
                    Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                            .deserialize("The folder was not found."));
                    return Optional.empty();
                }

                Set<File> fileList = this.folderController.getFolderListFiles(file);
                return fileList.stream()
                        .filter(value -> value.getName().equalsIgnoreCase(name + ".yml"))
                        .findFirst();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }
}
