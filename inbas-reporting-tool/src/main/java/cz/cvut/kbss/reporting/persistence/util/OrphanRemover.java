package cz.cvut.kbss.reporting.persistence.util;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.model.util.HasUri;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to remove orphan instances.
 * <p>
 * I.e. instances, which were removed from collections in updated entities. This utility class resolves the issue of
 * missing {@code orphanRemoval} attribute support in JOPA.
 */
public class OrphanRemover {

    private final EntityManager em;

    public OrphanRemover(EntityManager em) {
        this.em = Objects.requireNonNull(em);
    }

    /**
     * Compares the original and updated collection of instances and removes from repository those which are in original
     * but are not in the updates.
     *
     * @param originals Original collection
     * @param updates   Updated collection
     */
    public <T extends HasUri> void removeOrphans(Collection<T> originals, Collection<T> updates) {
        final Set<T> orphans = resolveOrphans(originals, updates);
        orphans.forEach(orphan -> {
            final T toRemove = em.merge(orphan);
            em.remove(toRemove);
        });
    }

    /**
     * Compares the original and updated collection and determines which of the elements were in the original but are not
     * in the update - the orphan elements.
     *
     * @param originals Original collection
     * @param updates   Updated collection
     * @return Set of detected orphans
     */
    public static <T extends HasUri> Set<T> resolveOrphans(Collection<T> originals, Collection<T> updates) {
        final Set<URI> updateUris = updates == null ? Collections.emptySet() :
                                    updates.stream().map(HasUri::getUri).collect(Collectors.toSet());
        return originals == null ? Collections.emptySet() :
               originals.stream().filter(m -> !updateUris.contains(m.getUri()))
                        .collect(Collectors.toSet());
    }
}
