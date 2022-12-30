package timnekk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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

    /**
     * Prints contents of a file
     *
     * @param file File to print
     * @throws FileNotFoundException if the file can not be read
     */
    public static void printFile(File file, PrintStream printStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            printStream.println(scanner.nextLine());
        }
    }
}
