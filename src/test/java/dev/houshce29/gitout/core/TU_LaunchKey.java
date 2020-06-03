package dev.houshce29.gitout.core;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

public class TU_LaunchKey {

    @Test
    public void testFire() {
        Nuke fakeNuke = new Nuke("/target/local/repo", Collections.emptySet(), false);
        BranchNuker fakeNuker = Mockito.mock(BranchNuker.class);
        Mockito.doNothing().when(fakeNuker).drop(fakeNuke);
        LaunchKey key = new LaunchKey(fakeNuke, fakeNuker);
        key.fire();

        Mockito.verify(fakeNuker).drop(fakeNuke);
    }
}
