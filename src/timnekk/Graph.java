package timnekk;

import timnekk.models.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Graph<E> {
    private final Set<Node<E>> nodes = new HashSet<>();
    private final DependenciesFinder<E> dependenciesFinder;

    public Graph(Set<E> items, DependenciesFinder<E> dependenciesFinder) {
        this.dependenciesFinder = dependenciesFinder;
        createNodes(items);
        addNodesDependencies();
    }

    private void createNodes(Set<E> items) {
        for (E item : items) {
            Node<E> node = new Node<>(item);
            nodes.add(node);
        }
    }

    private void addNodesDependencies() {
        for (Node<E> node : nodes) {
            Set<E> dependentItems = dependenciesFinder.findDependencies(node.getValue());

            for (E dependentItem : dependentItems) {

                for (Node<E> otherNode : nodes) {
                    E otherNodeItem = otherNode.getValue();
                    if (otherNodeItem.equals(dependentItem)) {
                        node.addDependentNode(otherNode);
                    }
                }

            }
        }
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
}
