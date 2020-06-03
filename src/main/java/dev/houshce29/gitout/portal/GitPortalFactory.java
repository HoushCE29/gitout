package dev.houshce29.gitout.portal;

import org.apache.commons.lang3.SystemUtils;

/**
 * Factory to get OS-specific git portal.
 */
public final class GitPortalFactory {
    private static final String FORMAT_WINDOWS_PREFIX = "cmd /c cd /D %s && ";

    private GitPortalFactory() {
    }

    /**
     * Returns a git portal for the project.
     * @param gitProjectDirectory Directory of project.
     * @return New git portal.
     */
    public static GitPortal get(String gitProjectDirectory) {
        return new GitPortal(getSystemCommandPortal(gitProjectDirectory));
    }

    private static CommandPortal getSystemCommandPortal(String gitProjectDirectory) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return new CommandPortal(String.format(FORMAT_WINDOWS_PREFIX, gitProjectDirectory));
        }
        else if (SystemUtils.IS_OS_UNIX) {
            // TODO add support for unix
            throw new UnsupportedOperationException("Unix not supported yet.");
        }
        throw new UnsupportedOperationException(SystemUtils.OS_NAME + " is not a support OS.");
    }
}
