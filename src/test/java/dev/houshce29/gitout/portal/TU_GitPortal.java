package dev.houshce29.gitout.portal;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class TU_GitPortal {

    private GitPortal gitPortal;
    private CommandPortal commandPortal;

    @Before
    public void beforeEach() {
        commandPortal = Mockito.mock(CommandPortal.class);
        gitPortal = new GitPortal(commandPortal);
    }

    @Test
    public void testListBranches() throws Exception {
        List<String> branches = Lists.newArrayList("feature/#1", "fix/#2", "* master");
        Mockito.when(commandPortal.execute("git branch"))
                .thenReturn(new CommandResponse(true, Lists.newArrayList(
                        "  feature/#1", "  fix/#2", "* master")));

        Assert.assertEquals(branches, gitPortal.listBranches());
    }

    @Test
    public void testListBranchesNotSuccessful() throws Exception {
        List<String> output = Collections.singletonList("FATAL: not a git repository.");
        Mockito.when(commandPortal.execute("git branch"))
                .thenReturn(new CommandResponse(false, output));

        try {
            gitPortal.listBranches();
            Assert.fail("Failed to throw exception.");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals(System.lineSeparator() + output.get(0), ex.getMessage());
        }
    }

    @Test
    public void testRemoveBranch() throws Exception {
        Mockito.when(commandPortal.execute("git branch -D feature/#1"))
                .thenReturn(new CommandResponse(true, Collections.emptyList()));

        Assert.assertTrue(gitPortal.removeBranch("feature/#1", false));
    }

    @Test
    public void testRemoveBranchOnlyIfMerged() throws Exception {
        Mockito.when(commandPortal.execute("git branch -d feature/#1"))
                .thenReturn(new CommandResponse(true, Collections.emptyList()));

        Assert.assertTrue(gitPortal.removeBranch("feature/#1", true));
    }

    @Test
    public void testRemoveBranchFailed() throws Exception {
        Mockito.when(commandPortal.execute("git branch -D feature/#1"))
                .thenReturn(new CommandResponse(false, Collections.emptyList()));

        Assert.assertFalse(gitPortal.removeBranch("feature/#1", false));
    }

    @Test
    public void testUnknownExecutionFailure() throws Exception {
        Mockito.when(commandPortal.execute("git branch")).thenThrow(new InterruptedException());

        try {
            gitPortal.listBranches();
            Assert.fail("Failed to throw exception.");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("Command [git branch] failed for unknown reason.", ex.getMessage());
            Assert.assertTrue(ex.getCause() instanceof InterruptedException);
        }
    }
}
