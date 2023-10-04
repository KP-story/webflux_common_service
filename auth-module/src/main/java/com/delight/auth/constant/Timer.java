package com.delight.auth.constant;

public interface Timer {
    String PER_5_SECOND_START_AT_0 = "0/5 * * * * ?";
    String PER_30_SECOND_START_AT_0 = "0/30 * * * * ?";
    String PER_20_MINUTES_START_AT_0 = "0 0/20 * * * ?";
    String WEEKLY_AT_SATURDAY_5AM = "0 0 5 * * SAT";
    String WEEKLY_AT_SATURDAY_4AM = "0 0 4 * * SAT";
    String WEEKLY_AT_SATURDAY_3AM = "0 0 3 * * SAT";
    String TEST_TIME = "0 40 13 * * ?";
}
