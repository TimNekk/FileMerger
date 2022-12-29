package timnekk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FileDependenciesFinder implements DependenciesFinder<File> {
    private final File root;

    public FileDependenciesFinder(File projectRoot) {
        this.root = projectRoot;
    }

    /**
     * Finds all dependencies of a file
     *
     * @param file File to find dependencies of
     * @return Set of files that the file depends on
     * @throws FileNotFoundException If the file is not found
     */
    public Set<File> findDependencies(File file) {
        Set<File> dependencies = new HashSet<>();

        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return null;
            // TODO: Handle exception
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            File dependency = findDependentFile(line);
            if (dependency != null) {
                dependencies.add(dependency);
            }
        }

        return dependencies;
    }

    /**
     * Finds a dependency in a line of code
     *
     * @param line Line of code
     * @return File if a dependency is found, null otherwise
     */
    private File findDependentFile(String line) {
        Pattern pattern = Pattern.compile("require ‘(.+)’");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String dependencyPath = matcher.group(1);
            return new File(root, dependencyPath);
        }

        return null;
    }
}
