package org.whirlplatform.java;

import com.google.gwt.core.shared.GwtIncompatible;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@GwtIncompatible
public class FileManager {

    File rootFolder;

    public FileManager(File rootFolder) {
        this.rootFolder = rootFolder;
    }

    public File getFile(String fileName) {
        return new File(rootFolder.getAbsolutePath() + File.separator
                + fileName);
    }

    // Если запрошенной директории нет, возвращает null
    public File[] listFiles(String folder) {
        File file;
        if (folder == null || folder.isEmpty()) {
            file = rootFolder;
        } else {
            file = new File(rootFolder.getAbsolutePath() + File.separator
                    + folder);
        }
        return file == null ? null : file.listFiles();
    }

    // В случае ошибки возвращать null?
    // Если файл существует, возвращать его?
    public File createFile(String dirName, String fileName) throws IOException {
        File file;
        if (dirName == null || dirName.isEmpty()) {
            file = new File(rootFolder.getAbsolutePath() + File.separator
                    + fileName);
        } else {
            Path dirPath = FileSystems.getDefault().getPath(
                    rootFolder.getAbsolutePath() + File.separator + dirName);
            Files.createDirectories(dirPath);
            file = new File(rootFolder.getAbsolutePath() + File.separator
                    + dirName + File.separator + fileName);

        }
        return file.createNewFile() ? file : null; // Если файл уже существует,
        // вернет null
    }
}
