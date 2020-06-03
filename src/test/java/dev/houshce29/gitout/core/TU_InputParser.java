package dev.houshce29.gitout.core;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TU_InputParser {
    private static final String TARGET = "C:\\dev\\gitout";
    private static final String EXCLUDE = "--exclude=[feature/#1,fix/#2]";
    private static final String MERGED_ONLY_BASE = "--mergedOnly=";

    private InputParser parser;

    @Before
    public void beforeEach() {
        parser = new InputParser();
    }

    @Test
    public void testParseNoOptions() {
        LaunchKey key = parser.parse(createArgs(TARGET));
        Assert.assertNotNull(key.getBranchNuker());
        Nuke result = key.getNuke();
        Assert.assertEquals(TARGET, result.getTarget());
        Assert.assertTrue(result.getExclusions().isEmpty());
        Assert.assertTrue(result.isOnlyForMerged());
    }

    @Test
    public void testParseAllOptions() {
        LaunchKey key = parser.parse(createArgs(TARGET, MERGED_ONLY_BASE, "false", EXCLUDE));
        Nuke result = key.getNuke();
        Assert.assertFalse(result.isOnlyForMerged());
        Assert.assertEquals(Sets.newHashSet("feature/#1", "fix/#2"), result.getExclusions());
    }

    @Test
    public void testParseMergedOnlyTrue() {
        LaunchKey key = parser.parse(createArgs(TARGET, MERGED_ONLY_BASE, "true"));
        Assert.assertTrue(key.getNuke().isOnlyForMerged());
    }

    @Test
    public void testParseMergedOnlyFalse() {
        LaunchKey key = parser.parse(createArgs(TARGET, MERGED_ONLY_BASE, "false"));
        Assert.assertFalse(key.getNuke().isOnlyForMerged());
    }

    @Test
    public void testParseExclude() {
        LaunchKey key = parser.parse(createArgs(TARGET, EXCLUDE));
        Nuke result = key.getNuke();
        Assert.assertEquals(Sets.newHashSet("feature/#1", "fix/#2"), result.getExclusions());
    }

    @Test
    public void testParseMergedOnlyIncorrectUsage() {
        try {
            parser.parse(createArgs(TARGET, MERGED_ONLY_BASE, "NULL"));
            Assert.fail("Failed to throw exception.");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("ERROR: Invalid 'mergedOnly' usage. Usage: --mergedOnly=<true|false>",
                    ex.getMessage());
        }
    }

    @Test
    public void testParseExcludeIncorrectUsage() {
        try {
            parser.parse(createArgs(TARGET, "--exclude=nothing"));
            Assert.fail("Failed to throw exception.");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("ERROR: Invalid 'exclude' usage. Usage: --exclude=[<branch1>, <branch2>, ...]",
                    ex.getMessage());
        }
    }

    @Test
    public void testParseExtraInput() {
        try {
            parser.parse(createArgs(TARGET, MERGED_ONLY_BASE, "false", EXCLUDE, "--hello=world"));
            Assert.fail("Failed to throw exception.");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("ERROR: Invalid arguments.", ex.getMessage());
            Assert.assertNotNull(ex.getCause());
            Assert.assertEquals("--hello=world", ex.getCause().getMessage());
        }
    }

    @Test
    public void testParseMissingTarget() {
        try {
            parser.parse(createArgs(""));
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("Missing path argument to Git project.", ex.getMessage());
        }
    }

    @Test
    public void testParseMissingArguments() {
        try {
            parser.parse(null);
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("Missing path argument to Git project.", ex.getMessage());
        }
    }

    private String[] createArgs(String target, String... additional) {
        List<String> args = new ArrayList<>(Arrays.asList(target.split("\\s+")));
        Arrays.stream(additional)
                .flatMap(arg -> Arrays.stream(arg.split("\\s+")))
                .forEach(args::add);
        return args.toArray(new String[0]);
    }
}
