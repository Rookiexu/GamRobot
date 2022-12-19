package cn.rookiex.record;

import cn.hutool.cron.CronUtil;
import cn.rookiex.observer.*;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RecordProcessor implements Runnable, Observable {

    private final List<Observer> observers = Lists.newCopyOnWriteArrayList();

    private final UpdateEvent event = new UpdateEventImpl(ObservedEvents.TICK_TIME);

    @Override
    public void run() {
        long cur = System.currentTimeMillis();
        event.put(ObservedParams.CUR_MS, cur);
        notify(event);
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void remove(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notify(UpdateEvent message) {
        for (Observer observer : observers) {
            try{
                observer.update(message);
            }catch (Exception e){
                log.error(e,e);
            }
        }
    }

    public void start() {
        CronUtil.schedule("* * * * * ?",this);
    }
}
