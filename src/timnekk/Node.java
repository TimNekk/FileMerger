package timnekk;

import java.util.HashSet;
import java.util.Set;

public class Node<E> {
    private final E value;
    private final Set<Node<E>> dependencies = new HashSet<>();

    public Node(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    public Set<Node<E>> getDependencies() {
        return dependencies;
    }

    public void addDependency(Node<E> dependency) {
        dependencies.add(dependency);
    }
}
