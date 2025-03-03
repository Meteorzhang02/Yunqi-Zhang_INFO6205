/*
 * Copyright (c) 2017. Phasmid Software
 */

package com.phasmidsoftware.dsaipg.huskySort.sort.huskySort;

import com.phasmidsoftware.dsaipg.huskySort.sort.huskySortUtils.HuskySequenceCoder;
import com.phasmidsoftware.dsaipg.huskySort.sort.huskySortUtils.HuskySortHelper;
import com.phasmidsoftware.dsaipg.huskySort.util.Config;
import com.phasmidsoftware.dsaipg.huskySort.util.LazyLogger;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.phasmidsoftware.dsaipg.util.SortBenchmarkHelper.regexLeipzig;

/**
 * Quick Benchmark Integration Test.
 * This is suitable for inclusion in unit tests.
 *
 * <p>
 * The expected time for a pure quicksort of N items and M runs is 1.39 k M N lg N (where lg represents log to the base 2).
 * Bear in mind that the Benchmark code does M/10 warmup runs also.
 */

@SuppressWarnings("ALL")
public class QuickBenchmarkIntegrationTest {

    @BeforeClass
    public static void BeforeClass() throws IOException {
        config = Config.load();
        benchmark = new HuskySortBenchmark(config);
        String huskysort = "huskysort";
        String name = config.get(huskysort, "version");
        huskyCoder = HuskySortHelper.getSequenceCoderByName(config.get(huskysort, "huskycoder", "Unicode"));
        logger.info("HuskySortBenchmark.main: " + name);
    }

    @Test
    public void testStrings1K() throws Exception {
        // NOTE: this is a very quick version of the other integration tests.
        String corpus = "eng-uk_web_2002_10K-sentences.txt";
        benchmark.benchmarkStringSorters(corpus, HuskySortBenchmarkHelper.getWords(corpus, line -> HuskySortBenchmarkHelper.splitLineIntoStrings(line, regexLeipzig, HuskySortBenchmarkHelper.REGEX_STRING_SPLITTER)), 1000, 100, huskyCoder);
    }

    @Test
    public void testDatesHalfK() throws Exception {
        benchmark.sortLocalDateTimes(500, 1000000);
    }

    @Test
    public void testStrings1KInstrumented() throws Exception {
        // NOTE: this is a very quick version of the other integration tests.
        benchmark.benchmarkStringSortersInstrumented(HuskySortBenchmarkHelper.getWords("eng-uk_web_2002_10K-sentences.txt", line -> HuskySortBenchmarkHelper.splitLineIntoStrings(line, regexLeipzig, HuskySortBenchmarkHelper.REGEX_STRING_SPLITTER)), 1000, 100, huskyCoder);
    }

    private static Logger logger = new LazyLogger(QuickBenchmarkIntegrationTest.class);
    private static HuskySortBenchmark benchmark;
    private static Config config;
    private static HuskySequenceCoder<String> huskyCoder;
}