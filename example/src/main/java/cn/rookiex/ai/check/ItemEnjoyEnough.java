package cn.rookiex.ai.check;


import cn.hutool.core.util.RandomUtil;
import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.Node;
import cn.rookiex.ai.node.deocrator.ConditionNode;
import cn.rookiex.robot.GameRobot;
import cn.rookiex.robot.ctx.RobotCtx;
import cn.rookiex.robot.playermanager.BagManager;
import cn.rookiex.robot.playermanager.SummonManager;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;


@IsNode
public class ItemEnjoyEnough extends ConditionNode {

    /**
     * id:itemId
     * value:need Random Count
     */
    private Map<Integer,Integer> enjoyItem;


    /**
     * 随机最大值
     */
    private int maxRandom = 100;

    @Override
    public boolean isTrue(AIContext context) {
        RobotCtx robotCtx = (RobotCtx) context;
        GameRobot gameRobot = robotCtx.getRobot().getGameRobot();

        SummonManager summonManager = gameRobot.getManager(SummonManager.class);
        BagManager bagManager = gameRobot.getManager(BagManager.class);

        int enjoyItem = summonManager.getEnjoyItem();
        if (enjoyItem != 0){
            boolean enough = bagManager.isEnough(enjoyItem, 1);
            if (enough){
                summonManager.setEnjoyItem(0);
            }else {
                return true;
            }
        }

        for (Integer id : this.enjoyItem.keySet()) {
            boolean enough = bagManager.isEnough(id, 1);
            if (!enough){
                Integer rate = this.enjoyItem.get(id);
                int i = RandomUtil.randomInt(maxRandom);
                if (i > rate){
                    summonManager.setEnjoyItem(id);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Node init(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) {
        JSONObject need = jsonObject.getJSONObject("enjoyItem");
        if (jsonObject.containsKey("maxRandom")){
            this.maxRandom = jsonObject.getIntValue("maxRandom");
        }
        this.enjoyItem = Maps.newHashMap();
        Set<String> strings = need.keySet();
        for (String string : strings) {
            int id = Integer.parseInt(string);
            int value = need.getIntValue(string);
            this.enjoyItem.put(id, value);
        }
        return this;
    }
}
