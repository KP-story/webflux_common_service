package com.delight.notify.constant;

public interface Timer {
    String PER_5_SECOND_START_AT_0 = "0/5 * * * * ?";
    String PER_30_SECOND_START_AT_0 = "0/30 * * * * ?";
    String PER_20_MINUTES_START_AT_0 = "0 0/20 * * * ?";
    String DAILY_AT_MIDDLE_NIGHT = "0 0 * * * *";
    String WEEKLY_AT_SATURDAY_4AM = "0 0 4 * * SAT";
}
