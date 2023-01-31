package cn.rookiex.robot.gamemanager;

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
