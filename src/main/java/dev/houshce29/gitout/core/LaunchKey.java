package dev.houshce29.gitout.core;

/**
 * Executable model that links a nuke to a branch nuker.
 */
public class LaunchKey {
    private final Nuke nuke;
    private final BranchNuker branchNuker;

    LaunchKey(Nuke nuke, BranchNuker branchNuker) {
        this.nuke = nuke;
        this.branchNuker = branchNuker;
    }

    /**
     * @return Nuke for this key.
     */
    public Nuke getNuke() {
        return nuke;
    }

    /**
     * @return Branch nuker for this key.
     */
    public BranchNuker getBranchNuker() {
        return branchNuker;
    }

    /**
     * Fires the branch nuker to drop the nuke.
     */
    public void fire() {
        branchNuker.drop(nuke);
    }
}
