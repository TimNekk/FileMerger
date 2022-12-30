package timnekk;

import timnekk.exceptions.FileWritingException;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public final class FileUtils {
    /**
     * Gets all files in the root directory and its subdirectories
     * If the root directory is a file, it will return a set with only that file
     *
     * @param directory Root directory
     * @return Set of files in the root directory and its subdirectories
     */
    public static Set<File> getFiles(File directory) {
        Set<File> files = new HashSet<>();

        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                files.addAll(getFiles(file));
            }
        } else {
            files.add(directory);
        }

        return files;
    }


    public static void addFileContentToFile(File file, File fileToAdd)
            throws FileNotFoundException, FileWritingException {
        Scanner scanner = new Scanner(file);

        try (Writer writer = new FileWriter(fileToAdd, true)) {
            while (scanner.hasNextLine()) {
                writer.write(scanner.nextLine());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FileWritingException("Could not write to file: " + fileToAdd.getAbsolutePath(), e);
        }
    }
}
