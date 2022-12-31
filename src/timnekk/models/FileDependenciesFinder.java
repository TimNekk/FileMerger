package timnekk.models;

import timnekk.exceptions.DependenciesGettingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Finds dependencies in a file
 */
public final class FileDependenciesFinder implements DependenciesFinder<File> {
    private final File root;

    /**
     * Creates an instance of the class
     *
     * @param projectRoot Root directory of the project
     */
    public FileDependenciesFinder(File projectRoot) {
        this.root = projectRoot;
    }

    /**
     * Finds all dependencies of a file
     *
     * @param file File to find dependencies of
     * @return Set of files that the file depends on
     * @throws DependenciesGettingException if the file can not be read
     */
    public Set<File> findDependencies(File file) throws DependenciesGettingException {
        Set<File> dependencies = new HashSet<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Optional<File> dependency = findDependentFile(line);
                dependency.ifPresent(dependencies::add);
            }
        } catch (FileNotFoundException e) {
            throw new DependenciesGettingException(
                    "Can not read file to find dependencies: " + file.getAbsolutePath(), e);
        }

        return dependencies;
    }

    /**
     * Finds a dependency in a line of code
     *
     * @param line Line of code
     * @return File if a dependency is found, null otherwise
     */
    private Optional<File> findDependentFile(String line) {
        Pattern pattern = Pattern.compile("require [‘'\"`](.+)[’'\"`]");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String dependencyPath = matcher.group(1);
            return Optional.of(new File(root, dependencyPath));
        }

        return Optional.empty();
    }
}
