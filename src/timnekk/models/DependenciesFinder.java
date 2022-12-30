package timnekk.models;

import timnekk.exceptions.CanNotGetDependenciesException;

import java.util.Set;

public interface DependenciesFinder<E> {
    /**
     * Finds all dependencies of an item
     *
     * @param item Item to find dependencies of
     * @return Set of items that the item depends on
     * @throws CanNotGetDependenciesException if dependencies can not be gotten
     */
    Set<E> findDependencies(E item) throws CanNotGetDependenciesException;
}
