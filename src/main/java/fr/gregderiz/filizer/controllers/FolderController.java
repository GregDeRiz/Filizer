package fr.gregderiz.filizer;

import com.google.common.collect.Sets;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

public final class FolderController {
    private final Set<File> files;
    private final FileManager fileManager;

    public FolderController(FileManager fileManager) {
        this.fileManager = fileManager;
        this.files = fileManager.getFiles();
    }

    public void loopFolder(File folder) {
        for (File file : getFolderListFiles(folder)) {
            if (Files.isDirectory(file.toPath())) {
                loopFolder(folder);
                continue;
            }

            this.fileManager.addFile(file, false);
        }
    }

    public Set<File> getFolderListFiles(File folder) {
        if (folder.listFiles() == null) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage()
                    .deserialize("The file is not a directory."));
            return Sets.newHashSet();
        }
        return Sets.newHashSet(folder);
    }

    public Optional<File> findFolderByName(String folderName) {
        return this.files.stream().filter(file -> file.getParentFile().getName().equalsIgnoreCase(folderName)).findFirst();
    }
}
