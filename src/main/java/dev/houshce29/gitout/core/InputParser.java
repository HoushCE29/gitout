package dev.houshce29.gitout.core;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service that does all the tough work of parsing command line arguments.
 */
public final class InputParser {
    private static final Pattern MERGED_ONLY_REGEX = Pattern.compile(".*(--mergedOnly\\s*=\\s*(true|false))(\\s+.*|$)");
    private static final Pattern EXCLUDE_REGEX = Pattern.compile(".*(--exclude\\s*=\\s*\\[(.*)\\])(\\s+.*|$)");

    /**
     * Parses out the args into a launch key.
     *
     * When the gradle script builds this into a distribution, the command will look
     * something like this:
     * <pre>
     * gitout C:\path\to\proj [--mergedOnly=true|false] [--exclude=[(branchNames),... ]]
     * </pre>
     *
     * @param args Arguments from the command line.
     * @return New launch key.
     */
    public LaunchKey parse(String[] args) {
        if (ArrayUtils.isEmpty(args) || StringUtils.isBlank(args[0])) {
            throw new RuntimeException("Missing path argument to Git project.");
        }
        String target = args[0];
        boolean mergedOnly = true;
        Set<String> exclusions = new HashSet<>();

        String options = String.join(" ", args).substring(target.length());

        if (options.contains("--mergedOnly")) {
            Matcher matcher = MERGED_ONLY_REGEX.matcher(options);
            if (!matcher.matches()) {
                throw new RuntimeException("ERROR: Invalid 'mergedOnly' usage. Usage: --mergedOnly=<true|false>");
            }
            mergedOnly = Boolean.parseBoolean(matcher.group(2));
            // Remove this from options
            options = options.replace(matcher.group(1), "");
        }

        if (options.contains("--exclude")) {
            Matcher matcher = EXCLUDE_REGEX.matcher(options);
            if (!matcher.matches()) {
                throw new RuntimeException(
                        "ERROR: Invalid 'exclude' usage. Usage: --exclude=[<branch1>, <branch2>, ...]");
            }
            exclusions = Arrays.stream(matcher.group(2).split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            // Remove this from options
            options = options.replace(matcher.group(1), "");
        }

        if (StringUtils.isNotBlank(options)) {
            throw new RuntimeException("ERROR: Invalid arguments.", new IllegalArgumentException(options.trim()));
        }
        return new LaunchKey(new Nuke(target, exclusions, mergedOnly), new BranchNuker());
    }
}
