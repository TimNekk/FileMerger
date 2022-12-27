package timnekk;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        File root = new File("root");
        FileWorker fileWorker = new FileWorker(root);

        // Get all files in the root directory
        Set<File> files = FileWorker.getFiles(root);

        // Create nodes
        Set<Node<File>> nodes = new HashSet<>();
        for (File file : files) {
            Node<File> node = new Node<>(file);
            nodes.add(node);
        }

        // Set dependencies
        for (Node<File> node : nodes) {
            Set<File> dependencies = fileWorker.getDependencies(node.getValue());

            for (File dependency : dependencies) {
                for (Node<File> dependencyNode : nodes) {
                    if (dependencyNode.getValue().equals(dependency)) {
                        node.addDependency(dependencyNode);
                    }
                }
            }
        }

        // Print dependencies
//        for (Node<File> node : nodes) {
//            System.out.println(node.getValue().getName());
//            for (Node<File> dependency : node.getDependencies()) {
//                System.out.println("\t" + dependency.getValue().getName());
//            }
//        }

        resolveDependencies(nodes, new HashSet<>());
    }

    private static void resolveDependencies(Set<Node<File>> nodes, Set<Node<File>> resolved) {
        for (Node<File> node : nodes) {
            if (!resolved.contains(node)) {
                resolveDependencies(node.getDependencies(), resolved);
                resolved.add(node);
                System.out.print(node.getValue().getName());
            }
        }
    }
}
