package cn.rookiex.robot.manager;

import cn.rookiex.robot.Manager;
import cn.rookiex.robot.PlayerManager;

/**
 * @author rookieX 2023/1/18
 */
@Manager
public class SummonManager implements PlayerManager {

    private int enjoyItem;

    public void setEnjoyItem(int enjoyItem) {
        this.enjoyItem = enjoyItem;
    }

    public int getEnjoyItem() {
        return enjoyItem;
    }
}
