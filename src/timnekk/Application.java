package timnekk;

import timnekk.exceptions.DependenciesGettingException;
import timnekk.exceptions.CircularDependencyException;
import timnekk.exceptions.FileWritingException;
import timnekk.models.DependenciesFinder;
import timnekk.models.FileDependenciesFinder;
import timnekk.models.Graph;
import timnekk.models.Node;

import java.io.*;
import java.util.*;

/**
 * Main class of the application.
 */
public final class Application {
    private final Scanner scanner;
    private final PrintStream printStream;

    /**
     * Creates an instance of the application.
     *
     * @param inputStream  Input stream to read from
     * @param outputStream Output stream to write to
     */
    public Application(InputStream inputStream, OutputStream outputStream) {
        scanner = new Scanner(inputStream);
        printStream = new PrintStream(outputStream);
    }

    /**
     * Runs the application.
     */
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
        printFilesMergingOrder(sortedFiles.get(), graph.get());

        mergeContent(sortedFiles.get(), output);
        printStream.println("\nFiles merged successfully to " + output.getAbsolutePath());
    }

    /**
     * Gets a file from the user.
     *
     * @param promptMessage Message to show to the user
     * @return File from the user
     */
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

    /**
     * Gets a sorted list of files in the graph
     *
     * @param graph Graph of files
     * @return Sorted list of files
     */
    private Optional<List<File>> getSortedListOfFiles(Graph<File> graph) {
        try {
            return Optional.of(graph.getSortedListOfItems());
        } catch (CircularDependencyException e) {
            printStream.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Prints the order in which the files should be merged and the dependencies of each file
     *
     * @param files List of files
     * @param graph Graph of files
     */
    private void printFilesMergingOrder(Collection<File> files, Graph<File> graph) {
        int i = 1;
        for (File file : files) {
            printStream.println(i + ". " + file.getName());
            i++;

            Optional<Node<File>> node = graph.getNode(file);
            if (node.isPresent()) {
                Set<Node<File>> dependentNodes = node.get().getDependentNodes();

                int j = 1;
                for (Node<File> dependentNode : dependentNodes) {
                    printStream.print((j == dependentNodes.size() ? "   └── " : "   ├── "));
                    printStream.println(dependentNode.getValue().getName());
                    j++;
                }
            }
        }
    }

    /**
     * Merges the content of the files into the output file
     *
     * @param files      List of files
     * @param outputFile Output file
     */
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

    /**
     * Prints the missing dependencies of the graph
     *
     * @param graph Graph of files
     */
    private void printMissingDependencies(Graph<File> graph) {
        int i = 1;
        for (File file : graph.getMissingDependencies()) {
            printStream.println(i + ". " + file);
            i++;
        }
    }
}
