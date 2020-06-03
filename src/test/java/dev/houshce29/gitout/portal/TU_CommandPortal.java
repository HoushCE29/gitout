package dev.houshce29.gitout.portal;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class TU_CommandPortal {
    private static final String OS_SPECIFIC_PREFIX = "cd /target/directory && ";
    private static final String COMMAND = "do-thing --include=[foo,bar,baz] --remote=http://dt.houshce29.dev/landing";
    private static final String EXPECTED_PROCESS_INVOCATION = OS_SPECIFIC_PREFIX + COMMAND;

    private CommandPortal portal;
    private Runtime runtime;

    @Before
    public void beforeEach() {
        portal = Mockito.spy(new CommandPortal(OS_SPECIFIC_PREFIX));
        runtime = Mockito.mock(Runtime.class);
        Mockito.doReturn(runtime).when(portal).runtime();
    }

    @Test
    public void testExecuteSuccessful() throws Exception {
        List<String> lines = Lists.newArrayList(
                "Processing request...", " Contacting remote server...", " Operation was successful!");
        mockProcessResult(true, lines.toArray(new String[0]));
        CommandResponse response = portal.execute(COMMAND);

        Mockito.verify(runtime).exec(EXPECTED_PROCESS_INVOCATION);
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(lines, response.getOutput());
    }

    @Test
    public void testExecuteFailure() throws Exception {
        List<String> lines = Lists.newArrayList(
                "Processing request...", " Contacting remote server...", "ERROR: Could not reach server!");
        mockProcessResult(false, lines.toArray(new String[0]));
        CommandResponse response = portal.execute(COMMAND);

        Mockito.verify(runtime).exec(EXPECTED_PROCESS_INVOCATION);
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(lines, response.getOutput());
    }

    private void mockProcessResult(boolean success, String... lines) throws Exception {
        byte[] bytes = Arrays.stream(lines)
                .reduce((s1, s2) -> s1 + System.lineSeparator() + s2)
                .orElseThrow(() -> new IllegalStateException("Failed to setup mock process input stream."))
                .getBytes();

        InputStream processStream = new ByteArrayInputStream(bytes);
        Process fakeProcess = Mockito.mock(Process.class);
        Mockito.when(fakeProcess.waitFor()).thenReturn(success ? 0 : 1);
        Mockito.when(fakeProcess.getInputStream()).thenReturn(processStream);
        Mockito.when(fakeProcess.getErrorStream()).thenReturn(processStream);

        Mockito.when(runtime.exec(Mockito.anyString())).thenReturn(fakeProcess);
    }
}
