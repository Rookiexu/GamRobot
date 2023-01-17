package cn.rookiex.robot;

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
}
