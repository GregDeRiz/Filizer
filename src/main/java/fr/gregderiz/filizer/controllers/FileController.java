package fr.gregderiz.filizer.controllers;

import fr.gregderiz.filizer.managers.FileManager;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public final class FileController {
    private final Set<File> files;

    public FileController(FileManager fileManager) {
        this.files = fileManager.getFiles();
    }

    /**
     * Return the first corresponding file of this name otherwise if no file is found. {@code findFirst()} will
     * return an empty {@code Optional}.
     * @param name the name of the file
     * @see Optional
     */
    public Optional<File> findFileByName(String name) {
        return this.files.stream().filter(file -> file.getName().equalsIgnoreCase(name + ".yml")).findFirst();
    }

    /**
     *  Return {@code true} if the file is found in the corresponding folder by they names otherwise if no file is found,
     *  return {@code false}.
     *
     *  <p>Additionally this function mean to check if the file is on a folder, for example, if the file is
     *  on the correct folder than we can manipulate it.
     *
     * @param folderName the name of the folder
     * @param fileName the name of the file
     * @see Optional
     */
    public boolean findFileInFolder(String folderName, String fileName) {
        Optional<File> optionalFile = findFileByName(fileName);
        return optionalFile.map(file -> file.getParentFile().getName().equalsIgnoreCase(folderName)).orElse(false);
    }
}
