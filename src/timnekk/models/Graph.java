package timnekk.models;

import timnekk.exceptions.DependenciesGettingException;
import timnekk.exceptions.CircularDependencyException;

import java.util.*;

/**
 * Graph class represents a graph of nodes with dependencies.
 *
 * @param <E> Type of the value of the nodes
 */
public final class Graph<E> {
    private final Set<Node<E>> nodes = new HashSet<>();
    private final Set<E> missingDependencies = new HashSet<>();
    private final DependenciesFinder<E> dependenciesFinder;

    /**
     * Creates a graph
     *
     * @param items              Items to create nodes for
     * @param dependenciesFinder Dependencies finder for the items
     * @throws DependenciesGettingException If dependencies can not be found
     */
    public Graph(Set<E> items, DependenciesFinder<E> dependenciesFinder) throws DependenciesGettingException {
        this.dependenciesFinder = dependenciesFinder;

        createNodes(items);
        addNodesDependencies();
    }

    /**
     * Creates nodes for all items without dependencies
     *
     * @param items Items to create nodes for
     */
    private void createNodes(Set<E> items) {
        for (E item : items) {
            Node<E> node = new Node<>(item);
            nodes.add(node);
        }
    }

    /**
     * Adds dependencies to the nodes
     *
     * @throws DependenciesGettingException If dependencies can not be found
     */
    private void addNodesDependencies() throws DependenciesGettingException {
        for (Node<E> node : nodes) {
            Set<E> dependentItems = dependenciesFinder.findDependencies(node.getValue());

            for (E dependentItem : dependentItems) {
                getNode(dependentItem).ifPresentOrElse(
                        node::addDependentNode,
                        () -> missingDependencies.add(dependentItem)
                );
            }
        }
    }

    /**
     * Gets a node from the graph
     *
     * @param item Item to get node for
     * @return Node for the item
     */
    public Optional<Node<E>> getNode(E item) {
        for (Node<E> node : nodes) {
            if (node.getValue().equals(item)) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets list of items sorted by dependencies.
     *
     * @return List of items sorted by dependencies.
     * @throws CircularDependencyException if circular dependency is found.
     */
    public List<E> getSortedListOfItems() throws CircularDependencyException {
        return getSortedListOfItems(nodes, new HashSet<>(), new HashSet<>());
    }

    /**
     * Gets list of items sorted by dependencies.
     *
     * @param nodes    Nodes to sort
     * @param resolved Resolved nodes
     * @param seen     Seen nodes
     * @return List of items sorted by dependencies.
     * @throws CircularDependencyException if circular dependency is found.
     */
    private List<E> getSortedListOfItems(Set<Node<E>> nodes, Set<Node<E>> resolved, Set<Node<E>> seen)
            throws CircularDependencyException {
        List<E> items = new ArrayList<>();

        for (Node<E> node : nodes) {
            if (resolved.contains(node)) {
                continue;
            }

            if (seen.contains(node)) {
                throw new CircularDependencyException("Circular dependency found in item: " + node.getValue());
            }

            seen.add(node);

            items.addAll(getSortedListOfItems(node.getDependentNodes(), resolved, seen));

            resolved.add(node);
            seen.remove(node);

            items.add(node.getValue());
        }

        return items;
    }

    /**
     * Gets missing dependencies
     *
     * @return Set of missing dependencies
     */
    public Set<E> getMissingDependencies() {
        return missingDependencies;
    }

    /**
     * Whether the graph has missing dependencies
     *
     * @return True if the graph has missing dependencies, false otherwise
     */
    public boolean hasMissingDependencies() {
        return !missingDependencies.isEmpty();
    }
}
