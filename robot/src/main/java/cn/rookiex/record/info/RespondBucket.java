package cn.rookiex.record.info;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author rookieX 2022/12/28
 */
@Getter
public class RespondBucket {

    private Map<Integer, Integer> costBucket = new ConcurrentSkipListMap<>();

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
            return 99999;
        }
    }

    public void addCost(int costTime){
        int i = computeDuration(costTime);
        costBucket.merge(i, 1, Integer::sum);
    }

    public int getAvgResp() {
        long total = 0;
        long totalCount = 0;
        for (Integer costTime : costBucket.keySet()) {
            Integer count = costBucket.get(costTime);
            total += costTime * count;
            totalCount += count;
        }
        return totalCount == 0 ? 0 : (int) (total / totalCount);
    }

    public int getRespTime(int rate) {
        long totalCount = 0;
        for (Integer costTime : costBucket.keySet()) {
            Integer count = costBucket.get(costTime);
            totalCount += count;
        }

        int needCount = (int) (rate * totalCount / 10000);

        int result = 0;
        for (Integer cost : costBucket.keySet()) {
            Integer count = costBucket.get(cost);
            needCount -= count;
            result = cost;
            if(needCount < 0){
                break;
            }
        }

        return result;
    }

    public int getSlowRespCount() {
        int count = 0;
        for (Integer integer : costBucket.keySet()) {
            if (integer > 200){
                count += costBucket.get(integer);
            }
        }
        return count;
    }
}
