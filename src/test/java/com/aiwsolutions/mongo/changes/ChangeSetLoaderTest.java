package com.aiwsolutions.mongo.changes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
        Set<Resource> files = changeSetLoader.loadChangeSetBsonResourcesFromPath("changes");
        assertThat(files.size(), is(1));
        assertThat(files.iterator().next().getFilename(), is("changeSet2.bson"));
    }

    @Test
    public void testLoadChangeSetClassesFromPackage() throws IOException {
        Set<Class<? extends ChangeSet>> classes = changeSetLoader.loadChangeSetClassesFromPackage("com.aiwsolutions.mongo.changes.sample");
        assertThat(classes.size(), is(1));
        assertThat(classes.iterator().next().getName(), is("com.aiwsolutions.mongo.changes.sample.ChangeSet1"));
    }
}
