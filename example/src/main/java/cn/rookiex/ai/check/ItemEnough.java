package cn.rookiex.ai.check;


import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.Node;
import cn.rookiex.ai.node.deocrator.ConditionNode;
import cn.rookiex.robot.GameRobot;
import cn.rookiex.robot.ctx.RobotCtx;
import cn.rookiex.robot.playermanager.BagManager;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;


@IsNode
public class ItemEnough extends ConditionNode {

    private Map<Integer,Integer> need;

    @Override
    public boolean isTrue(AIContext context) {
        RobotCtx robotCtx = (RobotCtx) context;
        GameRobot gameRobot = robotCtx.getRobot().getGameRobot();
        return gameRobot.getManager(BagManager.class).isEnough(need);
    }

    @Override
    public Node init(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) {
        JSONObject need = jsonObject.getJSONObject("need");
        this.need = Maps.newHashMap();
        Set<String> strings = need.keySet();
        for (String string : strings) {
            int id = Integer.parseInt(string);
            int value = need.getIntValue(string);
            this.need.put(id, value);
        }
        return this;
    }
}
