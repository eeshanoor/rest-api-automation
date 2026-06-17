package com.eeshanoor.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Custom retry analyzer — retries flaky tests up to MAX_RETRY times.
 * Usage: @Test(retryAnalyzer = RetryAnalyzer.class)
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRY = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && retryCount < MAX_RETRY) {
            retryCount++;
            System.out.printf("[RETRY] Attempt %d of %d for: %s%n",
                retryCount, MAX_RETRY, result.getName());
            return true;
        }
        return false;
    }
}