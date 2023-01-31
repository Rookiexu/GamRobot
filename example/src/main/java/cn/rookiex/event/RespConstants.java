package cn.rookiex.event;

import cn.rookiex.coon.SimpleMessage;
import cn.rookiex.message.Message;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2022/12/14
 */
@Log4j2
public class RespConstants implements RespGameEvent{

    public static final int ReqLogin = 1
            ,ReqSetName = 2
            ,ReqSkipMain = 3
            ,ReqItem = 4
            ,ReqHeroLevelUp = 5
            ,ReqHeroEquipGem = 6
            ,ReqRecharge = 7
            , ReqSummon = 8
            ;


    public static final int
            RespLogin = 1
            , RespSetName = 2
            , RespSkipMain = 3
            , RespItem = 4
            , RespHeroLevelUp = 5
            , RespHeroEquipGem = 6
            , RespRecharge = 7
            , RespSummon = 8

                    ;


    public static void dealResp0(Message message, RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        SimpleMessage simpleMessage = (SimpleMessage) message;

        int msgId = simpleMessage.getMsgId();
        String data = simpleMessage.parseData(String.class);
        log.info(robot.getFullName() + " deal msg : " + msgId + " , data : " + data);
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {

    }

    @Override
    public int eventId() {
        return 0;
    }
}
