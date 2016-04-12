package cz.cvut.kbss.inbas.reporting.model_new.util;

import cz.cvut.kbss.inbas.reporting.model_new.Event;
import cz.cvut.kbss.inbas.reporting.model_new.Factor;

import java.util.Set;

/**
 * Marker interface for classes used in factor graphs.
 */
public interface FactorGraphItem extends HasUri {

    Set<Event> getChildren();

    void setChildren(Set<Event> children);

    void addChild(Event child);

    Set<Factor> getFactors();

    void setFactors(Set<Factor> factors);

    void addFactor(Factor factor);
}
