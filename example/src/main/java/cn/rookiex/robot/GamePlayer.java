package cn.rookiex.robot;

import java.util.Map;

/**
 * @author rookieX 2023/1/16
 */
public class GamePlayer implements GameRobot{

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isEnough(Map<Integer, Integer> need) {
        return false;
    }
}
