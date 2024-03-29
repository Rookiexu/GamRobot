package cn.rookiex.event.recharge;

import cn.rookiex.message.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.GameRobot;
import cn.rookiex.robot.ItemConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.robot.gamemanager.BagManager;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2023/1/16
 */
@Log4j2
public class RespRecharge implements RespGameEvent {
    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
        Robot robot = robotContext.getRobot();
        GameRobot gameRobot = robot.getGameRobot();


        BagManager bagManager = gameRobot.getManager(BagManager.class);
        log.info(robot.getFullName() + " 拥有money : " + bagManager.getCount(ItemConstants.money) + " ,充值一个648");
        bagManager.addItem(ItemConstants.money, 648);
    }

    @Override
    public int eventId() {
        return RespConstants.RespRecharge;
    }
}
