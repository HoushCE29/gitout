package dev.houshce29.gitout.core;

import java.util.Collections;
import java.util.Set;

/**
 * Model to define how the stale/old branches get nuked.
 */
public class Nuke {
    private final String target;
    private final Set<String> exclusions;
    private final boolean onlyMerged;

    Nuke(String target, Set<String> exclusions, boolean onlyMerged) {
        this.target = target;
        this.exclusions = Collections.unmodifiableSet(exclusions);
        this.onlyMerged = onlyMerged;
    }

    /**
     * @return The local path of the target repo.
     */
    public String getTarget() {
        return target;
    }

    /**
     * @return Branches to exclude from deletion.
     */
    public Set<String> getExclusions() {
        return exclusions;
    }

    /**
     * @return `true` if only merged branches should be deleted.
     */
    public boolean isOnlyForMerged() {
        return onlyMerged;
    }
}
