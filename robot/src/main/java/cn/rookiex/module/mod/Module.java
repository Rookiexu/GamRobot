package cn.rookiex.module.mod;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.ctx.RobotContext;
import com.alibaba.fastjson.JSONObject;

/**
 * 执行模块,通过配置确认模块的执行方法,执行顺序(也可以乱序)
 *
 * @author rookieX 2022/12/5
 */
public interface Module {

    /**
     * 前置模块,顺序执行模块,随机执行模块
     */
    String PRE_MOD = "pre", ORDER_MOD = "order", AI_MOD = "ai";

    int PRE = 1, ORDER = 2, RANDOM = 3;

    String getName();

    /**
     * 0:random 1:order 2:pre
     *
     * @return 模块类型
     */
    int getSortType();

    /**
     * @return 执行顺序
     */
    int getOrder();

    void init(JSONObject config, ModuleManager moduleManager);

    ReqGameEvent getNextEvent(RobotContext context);

    void initRunEvent(RobotContext context);

    boolean isRunOut(RobotContext context);
}
