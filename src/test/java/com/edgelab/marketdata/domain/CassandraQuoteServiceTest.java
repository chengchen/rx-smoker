package com.edgelab.marketdata.domain;

import lombok.extern.slf4j.Slf4j;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestExecutionListeners(listeners = CassandraUnitTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
@CassandraDataSet(value = "schema.cql", keyspace = "marketdata")
@EmbeddedCassandra
@Slf4j
public class CassandraQuoteServiceTest {

    @Test
    public void test1() {
        log.info("####### EXEC 1");
    }

    @Test
    public void test2() {
        log.info("####### EXEC 2");
    }

    @Test
    public void test3() {
        log.info("####### EXEC 3");
    }

    @Test
    public void test4() {
        log.info("####### EXEC 4");
    }

    @Test
    public void test5() {
        log.info("####### EXEC 5");
    }

}
