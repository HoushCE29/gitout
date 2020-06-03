package dev.houshce29.gitout.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.houshce29.gitout.portal.GitPortal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TU_BranchNuker {
    private static final String TARGET = "/dev/fake/repos/gitout";
    private static final String MASTER_BRANCH = "master";
    private static final String ACTIVE_BRANCH = "* fix/#999";
    private static final List<String> BRANCHES = Collections.unmodifiableList(
            Lists.newArrayList(MASTER_BRANCH, ACTIVE_BRANCH,
                    "feature/#1", "feature/#2", "feature/#3", "feature/#4"));

    private BranchNuker nuker;
    private GitPortal portal;

    @Before
    public void beforeEach() {
        portal = Mockito.mock(GitPortal.class);
        Mockito.when(portal.listBranches()).thenReturn(BRANCHES);
        Mockito.when(portal.removeBranch(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(true);
        nuker = Mockito.spy(new BranchNuker());
        Mockito.doReturn(portal).when(nuker).getPortal(TARGET);
    }

    @Test
    public void testDropPlainNuke() {
        Nuke nuke = new Nuke(TARGET, Collections.emptySet(), false);
        nuker.drop(nuke);

        Mockito.verify(portal, Mockito.times(4))
                .removeBranch(Mockito.anyString(), Mockito.eq(false));
        Mockito.verify(portal, Mockito.never()).removeBranch(MASTER_BRANCH, false);
        Mockito.verify(portal, Mockito.never()).removeBranch(ACTIVE_BRANCH, false);
    }

    @Test
    public void testDropOnlyIfMergedNuke() {
        Nuke nuke = new Nuke(TARGET, Collections.emptySet(), true);
        nuker.drop(nuke);

        Mockito.verify(portal, Mockito.times(4))
                .removeBranch(Mockito.anyString(), Mockito.eq(true));
    }

    @Test
    public void testDropExclusionsNuke() {
        Set<String> exclude = Sets.newHashSet("feature/#2", "feature/#3");
        Nuke nuke = new Nuke(TARGET, exclude, false);
        nuker.drop(nuke);

        Mockito.verify(portal, Mockito.times(2))
                .removeBranch(Mockito.anyString(), Mockito.eq(false));
        Mockito.verify(portal, Mockito.never()).removeBranch(MASTER_BRANCH, false);
        Mockito.verify(portal, Mockito.never()).removeBranch(ACTIVE_BRANCH, false);
        Mockito.verify(portal, Mockito.never()).removeBranch("feature/#2", false);
        Mockito.verify(portal, Mockito.never()).removeBranch("feature/#3", false);
    }
}
