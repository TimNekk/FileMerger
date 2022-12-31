package timnekk;

import timnekk.exceptions.FileWritingException;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * A class that contains methods for working with files.
 */
public final class FileUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private FileUtils() {
    }

    /**
     * Gets all files in the root directory and its subdirectories
     * If the root directory is a file, it will return a set with only that file
     *
     * @param directory Root directory
     * @return Set of files in the root directory and its subdirectories
     */
    public static Set<File> getFiles(File directory) {
        Set<File> files = new HashSet<>();

        if (directory.isFile()) {
            files.add(directory);
            return files;
        }

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            files.addAll(getFiles(file));
        }
        return files;
    }

    /**
     * Reads a file and writes its content to another file (appends)
     *
     * @param file      File to read from
     * @param fileToAdd File to write to
     * @throws FileNotFoundException if the file can not be read
     * @throws FileWritingException  if the file can not be written to
     */
    public static void addFileContentToFile(File file, File fileToAdd)
            throws FileNotFoundException, FileWritingException {
        try (
                Scanner scanner = new Scanner(file);
                Writer writer = new FileWriter(fileToAdd, true)
        ) {
            while (scanner.hasNextLine()) {
                writer.write(scanner.nextLine());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FileWritingException("Could not write to file: " + fileToAdd.getAbsolutePath(), e);
        }
    }
}
