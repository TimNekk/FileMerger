package timnekk.models;

import timnekk.exceptions.DependenciesGettingException;

import java.util.Set;

/**
 * This interface is used to find dependencies of a given item.
 *
 * @param <E> type of the item to find dependencies for.
 */
public interface DependenciesFinder<E> {
    /**
     * Finds all dependencies of an item
     *
     * @param item Item to find dependencies of
     * @return Set of items that the item depends on
     * @throws DependenciesGettingException if dependencies can not be gotten
     */
    Set<E> findDependencies(E item) throws DependenciesGettingException;
}
