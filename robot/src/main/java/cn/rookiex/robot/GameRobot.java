package cn.rookiex.robot;


/**
 * @author rookieX 2023/1/16
 */
public interface GameRobot {

    <T extends PlayerManager> T getManager(Class<T> dataClass);

}
