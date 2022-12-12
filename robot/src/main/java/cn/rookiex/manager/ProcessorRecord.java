package cn.rookiex.manager;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rookieX 2022/12/12
 */
@Getter
public class ProcessorRecord {
    private final AtomicInteger totalCoon = new AtomicInteger();

    private final AtomicInteger totalRobot= new AtomicInteger();

    private final AtomicInteger totalLogin = new AtomicInteger();

    private final AtomicLong totalSend = new AtomicLong();

    private final AtomicLong totalResp = new AtomicLong();

    private final AtomicLong totalRespDeal = new AtomicLong();

    private final Map<Integer, AtomicInteger> waitMsg = Maps.newConcurrentMap();
}
