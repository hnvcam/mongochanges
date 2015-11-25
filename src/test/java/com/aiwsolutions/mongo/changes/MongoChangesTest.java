package com.aiwsolutions.mongo.changes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by choang on 11/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, MongoChangesConfig.class})
public class MongoChangesTest {
    @Autowired
    private MongoChanges mongoChanges;

    @Test
    public void testAutoRun() throws ChangeSetExecutionException {
        assertThat(mongoChanges.getExecutedChangeSet(), is(2));
        assertThat(mongoChanges.getSkippedChangeSet(), is(0));

        mongoChanges.executeChangeSets();

        assertThat(mongoChanges.getExecutedChangeSet(), is(0));
        assertThat(mongoChanges.getSkippedChangeSet(), is(2));
    }
}
