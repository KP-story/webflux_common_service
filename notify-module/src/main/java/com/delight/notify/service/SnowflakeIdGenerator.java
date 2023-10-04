package com.delight.notify.service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SnowflakeIdGenerator implements IdGenerator {
    private long instanceId;
    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    public SnowflakeIdGenerator(long instanceId) {
        if (instanceId > 1023L) {
            throw new IllegalArgumentException("instanceId too large");
        } else {
            this.instanceId = instanceId;
        }
    }

    public void updateInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for (timestamp = System.currentTimeMillis(); timestamp <= lastTimestamp; timestamp = System.currentTimeMillis()) {
        }

        return timestamp;
    }

    public synchronized Long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < this.lastTimestamp) {
            try {
                Thread.sleep(this.lastTimestamp - timestamp);
            } catch (InterruptedException var5) {
            }
        }

        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 4095L;
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        this.lastTimestamp = timestamp;
        long id = timestamp - 1288834974657L << 22 | this.instanceId << 12 | this.sequence;

        return id;
    }

    public Long genIdByTime(LocalDateTime time) {
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        return this.genIdByTime(zdt.toInstant().toEpochMilli());
    }

    public Long genIdByTime(long milliseconds) {
        return milliseconds - 1288834974657L << 22 | 0L | 0L;
    }
}
