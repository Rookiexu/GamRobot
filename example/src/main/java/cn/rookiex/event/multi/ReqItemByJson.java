package cn.rookiex.event.multi;

import cn.rookiex.coon.message.JsonMessage;
import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;
import com.alibaba.fastjson.JSONObject;

/**
 * @author rookieX 2022/12/6
 */
public class ReqItemByJson implements ReqGameEvent {

    @Override
    public int eventId() {
        return RespConstants.ReqItem;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",robot.getFullName());
        jsonObject.put("msg",robot.getFullName() + " 获取json道具");
        JsonMessage message = new JsonMessage(eventId(), jsonObject);
        robot.getChannel().writeAndFlush(message);
    }

    @Override
    public int waitId() {
        return RespConstants.RespItem;
    }
}
