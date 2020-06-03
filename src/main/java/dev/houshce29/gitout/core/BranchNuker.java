package dev.houshce29.gitout.core;

import com.google.common.annotations.VisibleForTesting;
import dev.houshce29.gitout.portal.GitPortal;
import dev.houshce29.gitout.portal.GitPortalFactory;

/**
 * Singleton that performs a nuke drop.
 */
public class BranchNuker {
    // Marker that Git typically decorates the active branch with.
    private static final String ACTIVE_MARKER = "*";

    // Default master branch that most repos have.
    private static final String DEFAULT_MASTER_BRANCH = "master";

    /**
     * Performs the branch nuke drop.
     * @param nuke Nuke to be dropped.
     */
    public void drop(Nuke nuke) {
        GitPortal portal = getPortal(nuke.getTarget());
        portal.listBranches().stream()
                // Filter out the active branch
                .filter(branch -> !branch.startsWith(ACTIVE_MARKER))
                // Filter out master
                .filter(branch -> !DEFAULT_MASTER_BRANCH.equalsIgnoreCase(branch))
                // Filter out exclusions
                .filter(branch -> !nuke.getExclusions().contains(branch))
                // Nuke each eligible branch
                .forEach(branch -> nukeBranch(branch, nuke.isOnlyForMerged(), portal));
    }

    private void nukeBranch(String branch, boolean onlyIfMerged, GitPortal portal) {
        if (portal.removeBranch(branch, onlyIfMerged)) {
            log("Removed branch [" + branch + "].");
        }
        else {
            log("Could not remove branch [" + branch + "].");
        }
    }

    private void log(Object out) {
        System.out.println(out);
    }

    @VisibleForTesting
    protected GitPortal getPortal(String target) {
        return GitPortalFactory.get(target);
    }
}
