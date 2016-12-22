package cz.cvut.kbss.reporting.persistence.dao.util;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.model.util.HasUri;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to remove orphaned instances.
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
     * Removes orphans which are present in the {@code originals} collection, but are not in the {@code updates}
     * collection from the ontology.
     *
     * @param originals Original instances
     * @param updates   Updated instances
     */
    public <T extends HasUri> void removeOrphans(Collection<T> originals, Collection<T> updates) {
        final Set<URI> updateUris = updates == null ? Collections.emptySet() :
                                    updates.stream().map(HasUri::getUri).collect(Collectors.toSet());
        final Set<T> orphans = originals == null ? Collections.emptySet() :
                               originals.stream().filter(m -> !updateUris.contains(m.getUri()))
                                        .collect(Collectors.toSet());
        orphans.forEach(em::remove);
    }
}
