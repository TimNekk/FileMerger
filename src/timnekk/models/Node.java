package timnekk.models;

import java.util.HashSet;
import java.util.Set;

public final class Node<E> {
    private final E value;
    private final Set<Node<E>> dependentNodes = new HashSet<>();

    public Node(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    public Set<Node<E>> getDependentNodes() {
        return dependentNodes;
    }

    public void addDependentNode(Node<E> dependency) {
        dependentNodes.add(dependency);
    }
}
