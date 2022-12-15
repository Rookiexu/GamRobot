package cn.rookiex.record;

import cn.hutool.Hutool;
import cn.hutool.cron.CronUtil;
import cn.rookiex.observer.Observable;
import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import cn.rookiex.observer.Observer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RecordProcessor implements Runnable, Observable {

    private List<Observer> observers = Lists.newCopyOnWriteArrayList();

    private Map<String, Object> infoMap = Maps.newHashMap();

    @Override
    public void run() {
        long cur = System.currentTimeMillis();
        infoMap.put(ObservedParams.CUR_MS, cur);
        notify(ObservedEvents.TICK_TIME, infoMap);
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
    public void notify(String message, Map<String, Object> infoMap) {
        for (Observer observer : observers) {
            try{
                observer.update(message, infoMap);
            }catch (Exception e){
                log.error(e,e);
            }
        }
    }

    public void start() {
        CronUtil.schedule("* * * * * ?",this);
    }
}
