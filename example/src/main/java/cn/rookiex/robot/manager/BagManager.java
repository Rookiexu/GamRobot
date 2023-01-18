package cn.rookiex.robot.manager;

import cn.rookiex.robot.PlayerManager;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author rookieX 2023/1/18
 */
public class BagManager implements PlayerManager {

    private Map<Integer, Integer> itemMap = Maps.newConcurrentMap();

    public void addItem(Map<Integer, Integer> need) {
        need.forEach(this::addItem);
    }

    public void addItem(int id, int count) {
        itemMap.merge(id, count, Integer::sum);
    }

    public boolean isEnough(Map<Integer, Integer> need) {
        for (Integer integer : need.keySet()) {
            Integer value = need.get(integer);
            if (!isEnough(integer, value)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEnough(int id, int count) {
        return itemMap.getOrDefault(id, 0) >= count;
    }

    public void removeItem(Map<Integer, Integer> need) {
        need.forEach(this::removeItem);
    }

    public void removeItem(int id, int count) {
        itemMap.merge(id, 0, (o, n) -> Math.max(o - count, 0));
    }
}
