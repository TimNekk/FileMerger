package timnekk;

import timnekk.exceptions.CanNotGetDependenciesException;
import timnekk.exceptions.CircularDependencyException;
import timnekk.models.DependenciesFinder;
import timnekk.models.FileDependenciesFinder;
import timnekk.models.Graph;

import java.io.*;
import java.util.*;

public final class Application {
    private final Scanner scanner;
    private final PrintStream printStream;

    public Application(InputStream inputStream, OutputStream outputStream) {
        scanner = new Scanner(inputStream);
        printStream = new PrintStream(outputStream);
    }

    public void run() {
        File root = getRootDirectory();
        getGraphOfFiles(root).ifPresent(this::printGraphInfo);
    }

    private File getRootDirectory() {
        printStream.println("Enter the path to the root directory: ");
        String path = scanner.nextLine();
        return new File(path);
    }

    /**
     * Gets a graph of all files in the root directory and its subdirectories
     *
     * @param root Root directory
     * @return Graph of all files
     */
    private Optional<Graph<File>> getGraphOfFiles(File root) {
        Set<File> files = FileUtils.getFiles(root);
        DependenciesFinder<File> dependenciesFinder = new FileDependenciesFinder(root);

        try {
            return Optional.of(new Graph<>(files, dependenciesFinder));
        } catch (CanNotGetDependenciesException e) {
            printStream.println(e.getMessage());
            return Optional.empty();
        }
    }

    private void printGraphInfo(Graph<File> graph) {
        Optional<List<File>> sortedFiles = getSortedListOfFiles(graph);

        if (sortedFiles.isEmpty()) {
            return;
        }

        if (graph.hasMissingDependencies()) {
            printStream.println("\nMissing dependencies: ");
            printMissingDependencies(graph);
        }

        printStream.println("\nFiles merging order:");
        printFilesMergingOrder(sortedFiles.get());

        printStream.println("\nMerged content:");
        printMergedContent(sortedFiles.get());
    }

    private Optional<List<File>> getSortedListOfFiles(Graph<File> graph) {
        try {
            return Optional.of(graph.getSortedListOfItems());
        } catch (CircularDependencyException e) {
            printStream.println(e.getMessage());
            return Optional.empty();
        }
    }

    private void printFilesMergingOrder(Collection<File> files) {
        int i = 1;
        for (File file : files) {
            printStream.println(i + ". " + file.getName());
            i++;
        }
    }

    private void printMergedContent(Collection<File> files) {
        Set<File> notReadFiles = new HashSet<>();
        for (File file : files) {
            try {
                FileUtils.printFile(file, printStream);
            } catch (FileNotFoundException e) {
                notReadFiles.add(file);
            }
        }

        if (!notReadFiles.isEmpty()) {
            printStream.println("\nThe following files could not be read:");
            for (File file : notReadFiles) {
                printStream.println(file);
            }
        }
    }

    private void printMissingDependencies(Graph<File> graph) {
        int i = 1;
        for (File file : graph.getMissingDependencies()) {
            printStream.println(i + ". " + file);
            i++;
        }
    }
}
