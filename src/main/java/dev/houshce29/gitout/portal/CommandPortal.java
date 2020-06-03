package dev.houshce29.gitout.portal;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal command line wrapper.
 */
class CommandPortal {
    private final String osSpecificPrefix;

    CommandPortal(String osSpecificPrefix) {
        this.osSpecificPrefix = osSpecificPrefix;
    }

    /**
     * Executes the given command in the current working directory.
     * @param command Command to execute.
     * @return Response from the command execution.
     * @throws IOException if there are issues accessing the input stream.
     * @throws InterruptedException if the process is interrupted during command execution.
     */
    public CommandResponse execute(String command) throws IOException, InterruptedException {
        final String fullCommand = osSpecificPrefix + command;
        Process process = runtime().exec(fullCommand);
        boolean success = process.waitFor() == 0;
        InputStream result = process.getInputStream();
        if (!success) {
            result = process.getErrorStream();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(result));
        List<String> output = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            output.add(line);
        }
        return new CommandResponse(success, output);
    }

    @VisibleForTesting
    protected Runtime runtime() {
        return Runtime.getRuntime();
    }
}
