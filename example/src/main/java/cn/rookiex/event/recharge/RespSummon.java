package cn.rookiex.event.recharge;

import cn.hutool.core.util.RandomUtil;
import cn.rookiex.message.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.GameRobot;
import cn.rookiex.robot.ItemConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.robot.gamemanager.BagManager;
import cn.rookiex.robot.gamemanager.SummonManager;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2023/1/16
 */
@Log4j2
public class RespSummon implements RespGameEvent {
    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
        Robot robot = robotContext.getRobot();

        GameRobot gameRobot = robot.getGameRobot();
        SummonManager summonManager = gameRobot.getManager(SummonManager.class);
        BagManager bagManager = gameRobot.getManager(BagManager.class);

        bagManager.removeItem(ItemConstants.money, 300);
        int enjoyItem = summonManager.getEnjoyItem();
        int i = RandomUtil.randomInt(100);
        if (i > 60){
            log.info(robot.getSimpleName() + " 随机值 : " + i + " ,抽到了 ： " + enjoyItem);
            bagManager.addItem(enjoyItem, 1);
        }else {
            log.info(robot.getSimpleName() + " 随机值 : " + i + " 没抽到 : " + enjoyItem + " ,余额 : " + bagManager.getCount(ItemConstants.money));
        }
    }

    @Override
    public int eventId() {
        return RespConstants.RespSummon;
    }
}
