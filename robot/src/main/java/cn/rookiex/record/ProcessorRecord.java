package cn.rookiex.record;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rookieX 2022/12/12
 */
@Getter
@ToString
public class ProcessorRecord {
    private final AtomicInteger totalCoon = new AtomicInteger();

    private final AtomicInteger totalRobot= new AtomicInteger();

    private final AtomicInteger totalLogin = new AtomicInteger();

    private final AtomicLong totalSend = new AtomicLong();

    private final AtomicLong totalResp = new AtomicLong();

    private final AtomicLong totalRespDeal = new AtomicLong();

    private final Map<Integer, AtomicInteger> waitMsg = Maps.newConcurrentMap();

    private final Map<Integer, AtomicInteger> sendMsg = Maps.newConcurrentMap();

    private final RespondBucket respCost = new RespondBucket();
}
