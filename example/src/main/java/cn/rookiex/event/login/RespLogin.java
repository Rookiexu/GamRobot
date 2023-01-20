package cn.rookiex.event.login;

import cn.rookiex.message.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.GamePlayer;
import cn.rookiex.robot.GameRobot;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2022/12/6
 */
@Log4j2
public class RespLogin implements RespGameEvent {
    @Override
    public int eventId() {
        return RespConstants.RespLogin;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
        Robot robot = robotContext.getRobot();

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setName(robot.getFullName());

        GameRobot gameRobot = robot.getGameRobot();
        if (gameRobot != null){
            log.error("账号重复登录 : " + robot.getFullName());
        }else {
            robot.setGameRobot(gamePlayer);
        }
    }
}
