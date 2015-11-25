package com.aiwsolutions.mongo.changes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by camhoang on 11/22/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class ChangeSetLoaderTest {
    @Autowired
    private ChangeSetLoader changeSetLoader;

    @Test
    public void testLoadFilesFromPath() throws IOException {
        List<File> files = changeSetLoader.loadChangeSetFilesFromPath("changes");
        assertThat(files.size(), is(1));
        assertThat(files.get(0).getName(), is("changeSet1.bson"));
    }

    @Test
    public void testLoadChangeSetClassesFromPackage() throws IOException {
        List<Class<? extends ChangeSet>> classes = changeSetLoader.loadChangeSetClassesFromPackage("com.aiwsolutions.mongo.changes.sample");
        assertThat(classes.size(), is(1));
        assertThat(classes.get(0).getName(), is("com.aiwsolutions.mongo.changes.sample.ChangeSet2"));
    }
}