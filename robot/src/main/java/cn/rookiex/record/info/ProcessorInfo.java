package cn.rookiex.record.info;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rookieX 2022/12/12
 */
@Getter
@ToString
public class ProcessorInfo {
    private final AtomicInteger totalCoon = new AtomicInteger();

    private final AtomicInteger totalRobot= new AtomicInteger();

    private final Set<String> robotName = Sets.newConcurrentHashSet();

    private final AtomicInteger totalLogin = new AtomicInteger();

    private final AtomicLong totalSend = new AtomicLong();

    private final AtomicLong totalResp = new AtomicLong();

    private final AtomicLong totalRespDeal = new AtomicLong();

    private final Map<Integer, AtomicInteger> waitMsg = Maps.newConcurrentMap();

    private final Map<Integer, AtomicInteger> sendMsg = Maps.newConcurrentMap();

    private final RespondBucket respCost = new RespondBucket();
}
