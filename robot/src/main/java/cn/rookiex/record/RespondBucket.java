package cn.rookiex.record;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author rookieX 2022/12/28
 */
public class RespondBucket {

    private Map<Integer, Integer> costBucket = Maps.newConcurrentMap();

    //0~100ms 每ms一个桶
    //100~200ms 每5ms一个桶
    //200~500ms 每20ms一个桶
    //>500ms一个桶

    public static int computeDuration(int duration) {
        if (duration < 1) {
            return 1;
        } else if (duration < 100) {
            return duration;
        } else if (duration < 200) {
            return duration - duration % 5;
        } else if (duration < 500) {
            return duration - duration % 20;
        } else if (duration < 1000) {
            return duration - duration % 100;
        } else if (duration < 5000) {
            return duration - duration % 200;
        } else if (duration < 10000) {
            return duration - duration % 500;
        } else {
            return 10001;
        }
    }

    public void addCost(int costTime){
        int i = computeDuration(costTime);
        costBucket.merge(i, 1, Integer::sum);
    }
}
