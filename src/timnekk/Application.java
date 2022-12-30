package timnekk;

import timnekk.exceptions.DependenciesGettingException;
import timnekk.exceptions.CircularDependencyException;
import timnekk.exceptions.FileWritingException;
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
        File root = getFileFromUser("Enter root directory: ");
        File output = getFileFromUser("Enter output file: ");

        Optional<Graph<File>> graph = getGraphOfFiles(root);
        if (graph.isEmpty()) {
            return;
        }

        Optional<List<File>> sortedFiles = getSortedListOfFiles(graph.get());
        if (sortedFiles.isEmpty()) {
            return;
        }

        if (graph.get().hasMissingDependencies()) {
            printStream.println("\nMissing dependencies: ");
            printMissingDependencies(graph.get());
        }

        printStream.println("\nFiles merging order:");
        printFilesMergingOrder(sortedFiles.get());

        mergeContent(sortedFiles.get(), output);
        printStream.println("\nFiles merged successfully to " + output.getAbsolutePath());
    }

    private File getFileFromUser(String promptMessage) {
        printStream.println(promptMessage);
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
        } catch (DependenciesGettingException e) {
            printStream.println(e.getMessage());
            return Optional.empty();
        }
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

    private void mergeContent(Collection<File> files, File outputFile) {
        Set<File> notReadFiles = new HashSet<>();
        for (File file : files) {
            try {
                FileUtils.addFileContentToFile(file, outputFile);
            } catch (FileNotFoundException e) {
                notReadFiles.add(file);
            } catch (FileWritingException e) {
                printStream.println(e.getMessage());
                return;
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
