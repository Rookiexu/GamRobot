package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;


/**
 * @author rookieX 2022/12/12
 */
public class PreStage implements ModuleStage {

    ModuleStage orderStage = new OrderStage();

    @Override
    public void setStage(ModuleStage stage) {
    }

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        ModuleManager moduleManager = robot.getModuleManager();
        if (robot.getCurModStage() == Module.PRE) {
            int curModIdx = robot.getCurModIdx();
            if (moduleManager.getPreModule().size() == curModIdx + 1) {
                Module module = moduleManager.getPreModule().get(curModIdx);
                return module.isRunOut(robotContext);
            }
        }
        return false;
    }

    @Override
    public boolean isModOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        ModuleManager moduleManager = robot.getModuleManager();
        int curModIdx = robot.getCurModIdx();
        if (moduleManager.getPreModule().size() > curModIdx) {
            Module module = moduleManager.getPreModule().get(curModIdx);
            return module.isRunOut(robotContext);
        }
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurEventIdx(0);

        ModuleManager moduleManager = robot.getModuleManager();
        int curModIdx = robot.getCurModIdx();
        Module module = moduleManager.getPreModule().get(curModIdx);
        module.initRunEvent(robotContext);
    }

    @Override
    public void initStage(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModStage(Module.PRE);
        robot.setCurModIdx(0);

        initMod(robotContext);
    }

    @Override
    public ModuleStage nextStage(RobotContext robotContext) {
        return orderStage;
    }

    @Override
    public void toNextMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModIdx(robot.getCurModIdx() + 1);
    }

    @Override
    public ReqGameEvent getEvent(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        int curModIdx = robot.getCurModIdx();
        ModuleManager moduleManager = robot.getModuleManager();
        if (moduleManager.getPreModule().size() <= curModIdx) {
            return null;
        }
        Module module = moduleManager.getPreModule().get(curModIdx);
        return module.getNextEvent(robotContext);
    }
}
