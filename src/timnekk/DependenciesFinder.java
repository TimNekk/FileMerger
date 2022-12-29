package timnekk;

import java.util.Set;

public interface DependenciesFinder<E> {
    Set<E> findDependencies(E item);
}
