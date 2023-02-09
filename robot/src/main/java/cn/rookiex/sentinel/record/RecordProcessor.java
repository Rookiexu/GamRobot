package cn.rookiex.sentinel.record;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.sentinel.pubsub.*;
import cn.rookiex.sentinel.pubsub.cons.SystemEventsKeys;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.AbstractQueue;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RecordProcessor implements Runnable, EventBus {

    private final List<Subscriber> subscribers = Lists.newCopyOnWriteArrayList();

    private final SystemEvent event = new SystemEventImpl(SystemEventsKeys.TICK_TIME);

    private final AbstractQueue<SystemEvent> eventQue = Queues.newConcurrentLinkedQueue();

    private RobotManager robotManager;

    @Override
    public void run() {
        log.info("记录线程开始执行");
        while (!robotManager.isStop()){
            try {
                SystemEvent poll = eventQue.poll();
                if (poll != null) {
                    for (Subscriber subscriber : subscribers) {
                        subscriber.update(poll);
                    }
                }
            }catch (Exception e){
                log.error(e,e);
            }
        }
        log.info("记录线程执行结束退出");
    }

    public void tickLog(){
        for (Subscriber subscriber : subscribers) {
            if (subscriber instanceof LogSubscriber){
                ((LogSubscriber) subscriber).logInfo();
            }
        }
    }

    @Override
    public void subscribe(Subscriber o) {
        subscribers.add(o);
    }

    @Override
    public void publish(SystemEvent message) {
        eventQue.offer(message);
    }

    public void start() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(this);
        CronUtil.schedule("0 0/1 * * * ?", (Task) this::tickLog);
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }
}
