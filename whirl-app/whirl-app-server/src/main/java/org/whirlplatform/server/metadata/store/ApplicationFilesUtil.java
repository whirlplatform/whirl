package org.whirlplatform.server.metadata.store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.NonWritableChannelException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FileElement.InputStreamProvider;
import org.whirlplatform.meta.shared.editor.FileElementCategory;


public class ApplicationFilesUtil {

    public static final int WAIT_FOR_UNLOCK_IN_SECONDS = 600;

    /**
     * Блокирует доступ к текстовому файлу и заменяет его содержимое на content. После записи
     * блокировка снимается. Если файл уже заблокирован, каждую секунду проверяет не освободился ли
     * он в течении 10 минут. Если не освободился, то бросает исключение IOException.
     *
     * @param file    - путь к файлу
     * @param content - содержимое файла
     * @throws IOException
     */
    public static void writeTextFile(final Path file, final String content) throws IOException {
        try (FileOutputStream output = new FileOutputStream(file.toFile(), false)) {
            try (FileChannel channel = output.getChannel()) {
                try (FileLock lock = getFileLock(channel, false, WAIT_FOR_UNLOCK_IN_SECONDS)) {
                    if (lock == null) {
                        throw new IOException(String.format("Timeout writing file unlocking '%s'",
                                file.toString()));
                    }
                    byte[] bytesToSave = content.getBytes("UTF-8");
                    output.write(bytesToSave);
                    output.flush();
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException(
                    String.format("The file for writing is not found: '%s'", file.toString()));
        }
    }

    /**
     * Блокирует доступ к текстовому файлу и читает его содержимое. После чтения блокировка
     * снимается. Если файл уже заблокирован, каждую секунду проверяет не освободился ли он в
     * течении 10 минут. Если не освободился, то бросает исключение IOException.
     *
     * @param file - путь к файлу
     * @return Содержимое файла в виде строки
     * @throws IOException
     */
    public static String readTextFile(final Path file) throws IOException {
        String result = null;
        try (FileInputStream input = new FileInputStream(file.toFile())) {
            try (FileChannel channel = input.getChannel()) {
                try (FileLock lock = getFileLock(channel, true, WAIT_FOR_UNLOCK_IN_SECONDS)) {
                    if (lock == null) {
                        throw new IOException(String.format("Timeout reading file unlocking '%s'",
                                file.toString()));
                    }
                    result = IOUtils.toString(input, "UTF-8");
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException(
                    String.format("The file for reading is not found: '%s'", file.toString()));
        }
        return result;
    }

    @SuppressWarnings("resource")
    private static FileLock getFileLock(final FileChannel channel, boolean shared,
                                        int waitForAvailabilityInSeconds)
            throws IOException {
        FileLock result = null;
        try {
            int seconds = waitForAvailabilityInSeconds;
            result = channel.tryLock(0L, Long.MAX_VALUE, shared);
            while (result == null && seconds > 0) {
                Thread.sleep(1000);
                result = channel.tryLock(0L, Long.MAX_VALUE, shared);
                seconds--;
            }
        } catch (IOException e) {
            throw new IOException("The attempt to lock of the file was unsuccessful", e);
        } catch (InterruptedException e) {
            throw new IOException("The thread sleep was interrupted", e);
        } catch (NonWritableChannelException e) {
            throw new IOException("The file channel is not writable", e);
        }
        return result;
    }

    public static Map<FileElement, Exception> saveApplicationFiles(Path newAppPath,
                                                                   ApplicationElement application) {
        Map<FileElement, Exception> errors = new HashMap<>();
        for (final FileElement file : application.getJavaScriptFiles()) {
            saveApplicationFile(newAppPath, FileElementCategory.JAVASCRIPT, file, errors);
        }
        for (final FileElement file : application.getCssFiles()) {
            saveApplicationFile(newAppPath, FileElementCategory.CSS, file, errors);
        }
        for (final FileElement file : application.getJavaFiles()) {
            saveApplicationFile(newAppPath, FileElementCategory.JAVA, file, errors);
        }
        for (final FileElement file : application.getImageFiles()) {
            saveApplicationFile(newAppPath, FileElementCategory.IMAGE, file, errors);
        }
        if (application.getStaticFile() != null) {
            saveApplicationFile(newAppPath, FileElementCategory.STATIC, application.getStaticFile(),
                    errors);
        }
        return errors;
    }

    public static void saveApplicationFile(Path newAppPath, FileElementCategory category,
                                           FileElement file,
                                           Map<FileElement, Exception> errors) {
        try (InputStream is = (InputStream) file.getInputStream()) {
            if (is == null) {
                errors.put(file, new NoSuchFileException("File input stream is null"));
                return;
            }
            Path dir = createFolderPath(newAppPath, category);
            Path targetFile = dir.resolve(file.getFileName());
            if (Files.notExists(targetFile)) {
                Files.copy(is, targetFile);
            }
        } catch (IOException e) {
            errors.put(file, e);
        }
    }

    public static Map<FileElement, Exception> saveApplicationDataFiles(
            Collection<FileElement> dataFiles,
            Path applicationPath) {
        Map<FileElement, Exception> errors = new HashMap<>();
        for (final FileElement file : dataFiles) {
            saveApplicationFile(applicationPath, FileElementCategory.DATA, file, errors);
        }
        return errors;
    }

    public static List<FileElement> getApplicationDataFiles(final Path applicationPath)
            throws IOException {
        List<FileElement> result = new ArrayList<>();
        Path dir = buildFolderPath(applicationPath, FileElementCategory.DATA);
        if (!Files.exists(dir)) {
            return result;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (!Files.isDirectory(file)) {
                    result.add(createDataFileElement(file, applicationPath));
                }
            }
        }
        return result;
    }

    private static FileElement createDataFileElement(final Path filePath,
                                                     final Path applicationPath) {
        FileElement result = new FileElement();
        result.setFileName(filePath.getFileName().toString());
        result.setCategory(FileElementCategory.DATA);
        updateInputStream(applicationPath, result);
        return result;
    }

    public static void saveApplicationFilesAs(Path oldAppPath, Path newAppPath,
                                              ApplicationElement application) {
        loadApplicationFiles(oldAppPath, application);
        try {
            List<FileElement> dataFiles = getApplicationDataFiles(oldAppPath);
            saveApplicationDataFiles(dataFiles, newAppPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveApplicationFiles(newAppPath, application);
    }

    public static void loadApplicationFiles(Path applicationPath, ApplicationElement application) {
        updateInputStream(applicationPath, application.getJavaScriptFiles());
        updateInputStream(applicationPath, application.getCssFiles());
        updateInputStream(applicationPath, application.getJavaFiles());
        updateInputStream(applicationPath, application.getImageFiles());
        if (application.getStaticFile() != null) {
            updateInputStream(applicationPath, application.getStaticFile());
        }
    }

    private static void updateInputStream(final Path applicationPath,
                                          Collection<FileElement> files) {
        for (final FileElement file : files) {
            updateInputStream(applicationPath, file);
        }
    }

    private static void updateInputStream(final Path applicationPath, FileElement file) {
        if (applicationPath == null) {
            throw new IllegalArgumentException("Application path is null");
        }
        if (file == null) {
            throw new IllegalArgumentException("FileElement is null");
        }
        if (file.getCategory() == null) {
            throw new IllegalArgumentException("FileElement Category is null");
        }
        final Path filePath = buildFilePath(applicationPath, file);
        if (Files.exists(filePath)) {
            file.setInputStreamProvider(new InputStreamProvider() {
                @Override
                public Object get() throws IOException {
                    return Files.newInputStream(filePath);
                }

                @Override
                public String path() {
                    return filePath.toString();
                }
            });
        } else {
            file.setInputStreamProvider(null);
        }
    }

    /**
     * Creates folders chain and makes a copy of the specified file element.
     *
     * @throws IOException
     */
    public static void copyApplicationFile(final String srcAppRoot, final FileElement srcFile,
                                           final String dstAppRoot)
            throws IOException {
        copyApplicationFile(Paths.get(srcAppRoot), srcFile, Paths.get(dstAppRoot));
    }

    public static void copyApplicationFile(final Path srcAppPath, final FileElement srcFile,
                                           final Path dstAppPath)
            throws IOException {
        Path targetDir = createFolderPath(dstAppPath, srcFile.getCategory());
        Path targetFile = targetDir.resolve(srcFile.getFileName());
        updateInputStream(srcAppPath, srcFile);
        InputStream inputStream = (InputStream) srcFile.getInputStream();
        if (inputStream != null) {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static Path buildFolderPath(final String appRoot, final FileElementCategory category) {
        return buildFolderPath(Paths.get(appRoot), category);
    }

    public static Path buildFolderPath(final Path appPath, final FileElementCategory category) {
        return appPath.resolve(buildCategoryRelativePath(category));
    }

    public static Path buildFilePath(final String appRoot, final FileElement file) {
        return buildFilePath(Paths.get(appRoot), file);
    }

    public static Path buildFilePath(final Path appPath, final FileElement file) {
        return appPath.resolve(buildFileRelativePath(file));
    }

    /**
     * Builds the path and create folders to the category
     *
     * @param appRoot
     * @param category
     * @return Path
     * @throws IOException
     */
    public static Path createFolderPath(final String appRoot, final FileElementCategory category)
            throws IOException {
        return createFolderPath(Paths.get(appRoot), category);
    }

    /**
     * Builds the path and create folders to the category
     *
     * @param appPath
     * @param category
     * @return Path
     * @throws IOException
     */
    public static Path createFolderPath(final Path appPath, final FileElementCategory category)
            throws IOException {
        Path result = appPath.resolve(buildCategoryRelativePath(category));
        if (!Files.exists(result)) {
            Files.createDirectories(result);
        }
        return result;
    }

    /**
     * Creates the path to the file element
     *
     * @return path
     */
    public static Path buildFileRelativePath(final FileElement file) {
        return buildCategoryRelativePath(file.getCategory()).resolve(file.getFileName());
    }

    /**
     * Creates path to the file element category
     *
     * @param category
     * @return path
     */
    public static Path buildCategoryRelativePath(FileElementCategory category) {
        return Paths.get(category.toString());
    }

    public static InputStream getInputStream(final Path applicationPath,
                                             final FileElementCategory category,
                                             final String fileName) throws IOException {
        Path filePath = buildFolderPath(applicationPath, category).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("Application file does not exist: " + filePath.toString());
        }
        return Files.newInputStream(filePath, StandardOpenOption.READ);
    }
}
