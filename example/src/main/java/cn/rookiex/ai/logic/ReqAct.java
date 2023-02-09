package cn.rookiex.ai.logic;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.Node;
import cn.rookiex.ai.node.action.ActNode;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.ctx.RobotCtx;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @author rookieX 2023/2/9
 */
@IsNode
@Log4j2
public class ReqAct extends ActNode {

    private String eventName;

    @Override
    public TreeStates execute0(AIContext context) {
        RobotCtx aiContext = (RobotCtx) context;
        Map<String, ReqGameEvent> reqEventMap = ((RobotCtx) context).getRobotManager().getModuleManager().getReqEventMap();
        ReqGameEvent reqGameEvent = reqEventMap.get(this.eventName);
        if (reqGameEvent == null){
            log.error("ai 请求 eventName 不存在 : " + this.eventName);
        }
        aiContext.setReqEvent(reqGameEvent);
        return TreeStates.IS_RUN;
    }

    @Override
    public Node init(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) throws IllegalAccessException, InstantiationException {
        super.init(jsonObject, stringClassMap);
        this.eventName = jsonObject.getString("req");
        return this;
    }
}
