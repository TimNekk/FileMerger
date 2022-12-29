package timnekk;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
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
}
