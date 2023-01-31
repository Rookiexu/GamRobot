package cn.rookiex.robot;


import cn.rookiex.robot.gamemanager.PlayerManager;

/**
 * @author rookieX 2023/1/16
 */
public interface GameRobot {

    <T extends PlayerManager> T getManager(Class<T> dataClass);

}
