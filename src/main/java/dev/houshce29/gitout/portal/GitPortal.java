package dev.houshce29.gitout.portal;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Portal to Git execution.
 */
public class GitPortal {
    private final CommandPortal portal;

    GitPortal(CommandPortal portal) {
        this.portal = portal;
    }

    /**
     * Lists the current branches.
     * @return List of current branches.
     */
    public List<String> listBranches() {
        CommandResponse response = execute("branch");
        if (!response.isSuccessful()) {
            throw new RuntimeException(System.lineSeparator()
                    + String.join(System.lineSeparator(), response.getOutput()));
        }
        return response.getOutput().stream()
                // Remove any display padding
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Maybe removes a branch.
     * @param branch Branch to maybe remove.
     * @param onlyIfMerged  Flag to denote to remove only if merged.
     * @return Whether or not the branch was removed.
     */
    public boolean removeBranch(String branch, boolean onlyIfMerged) {
        String option = onlyIfMerged ? " -d " : " -D ";
        return execute("branch" + option + branch).isSuccessful();
    }

    private CommandResponse execute(String gitCommand) {
        final String command = "git " + gitCommand;
        try {
            return portal.execute(command);
        }
        catch (InterruptedException | IOException ex) {
            throw new RuntimeException("Command [" + command + "] failed for unknown reason.", ex);
        }
    }
}
