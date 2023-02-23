package cn.rookiex.v2.coon.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rookieX 2023/2/22
 */
public class QueueManager {

    public static final QueueManager instance = new QueueManager();

    public static QueueManager getInstance() {
        return instance;
    }

    /**
     * todo 添加初始化策略
     */
    private Map<String, ExecutorService> queueExecutorMap = new HashMap<>();

    private Map<String, ProtocolQueue> queueMap = new ConcurrentHashMap<>();

    public ProtocolQueue getPlayerQueue(long sessionId) {
        return getQueue("player", sessionId);
    }

    private ProtocolQueue getQueue(String name, long id) {
        //todo 添加策略提供不同的线程获取方案,现在先单线程运行
        ExecutorService queueExecutor = queueExecutorMap.computeIfAbsent(name, (k) -> Executors.newSingleThreadExecutor());
        return queueMap.computeIfAbsent(name, (k) -> new ProtocolQueue(name, queueExecutor));
    }
}
