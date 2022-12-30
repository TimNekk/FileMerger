package timnekk.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node in a graph
 *
 * @param <E> Type of the value
 */
public final class Node<E> {
    private final E value;
    private final Set<Node<E>> dependentNodes = new HashSet<>();

    /**
     * Creates a node
     *
     * @param value Value of the node
     */
    public Node(E value) {
        this.value = value;
    }

    /**
     * Gets the value of the node
     *
     * @return Value of the node
     */
    public E getValue() {
        return value;
    }

    /**
     * Gets the dependent nodes
     *
     * @return Dependent nodes
     */
    public Set<Node<E>> getDependentNodes() {
        return dependentNodes;
    }

    /**
     * Adds a dependent node
     *
     * @param dependency Dependent node
     */
    public void addDependentNode(Node<E> dependency) {
        dependentNodes.add(dependency);
    }
}
