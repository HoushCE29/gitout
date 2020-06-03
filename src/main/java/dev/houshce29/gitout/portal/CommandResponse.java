package dev.houshce29.gitout.portal;

import java.util.Collections;
import java.util.List;

/**
 * Response from a command invocation.
 */
public final class CommandResponse {
    private final boolean successful;
    private final List<String> output;

    public CommandResponse(boolean successful, List<String> output) {
        this.successful = successful;
        this.output = Collections.unmodifiableList(output);
    }

    /**
     * @return `true` if the command was successful.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * @return Output from the command, line-by-line.
     */
    public List<String> getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "Success: " + successful + System.lineSeparator() +
                String.join(System.lineSeparator(), output);
    }
}
