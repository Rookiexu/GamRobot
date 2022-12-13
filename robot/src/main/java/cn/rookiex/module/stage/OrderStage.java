package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

import java.util.List;

/**
 * @author rookieX 2022/12/12
 */
public class OrderStage implements ModuleStage {

    ModuleStage stage = new RandomStage();

    @Override
    public void setStage(ModuleStage stage) {
    }

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        if (robot.getCurModStage() == Module.ORDER) {
            int curModIdx = robot.getCurModIdx();
            List<Module> allOrderModules = robot.getAllOrderModules();
            if (allOrderModules.size() == curModIdx + 1) {
                Module module = allOrderModules.get(curModIdx);
                return module.isRunOut(robotContext);
            }
        }
        return false;
    }

    @Override
    public boolean isModOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        int curModIdx = robot.getCurModIdx();
        List<Module> allOrderModules = robot.getAllOrderModules();
        if (allOrderModules.size() > curModIdx) {
            Module module = allOrderModules.get(curModIdx);
            return module.isRunOut(robotContext);
        }
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModStage(Module.ORDER);

        int curModIdx = robot.getCurModIdx();
        if (robot.getAllOrderModules().size() > curModIdx) {
            Module module = robot.getAllOrderModules().get(curModIdx);
            module.initRunEvent(robotContext);
        }
    }

    @Override
    public ModuleStage nextStage(RobotContext robotContext) {
        return stage;
    }

    @Override
    public ReqGameEvent getEvent(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        int curModIdx = robot.getCurModIdx();
        if (robot.getAllOrderModules().size() <= curModIdx) {
            return null;
        }
        Module module = robot.getAllOrderModules().get(curModIdx);
        return module.getNextEvent(robotContext);
    }
}
