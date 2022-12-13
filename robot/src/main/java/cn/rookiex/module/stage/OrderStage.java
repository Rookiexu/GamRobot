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

        ModuleManager moduleManager = robot.getModuleManager();
        if (robot.getCurModStage() == Module.ORDER) {
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
        List<Module> orderModule = moduleManager.getOrderModule();
        List<Module> randomModules = robot.getRandomModules();

        if (moduleManager.getPreModule().size() > curModIdx) {
            Module module = moduleManager.getPreModule().get(curModIdx);
            return module.isRunOut(robotContext);
        }
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModStage(Module.ORDER);

        ModuleManager moduleManager = robot.getModuleManager();
        int curModIdx = robot.getCurModIdx();
        if (moduleManager.getPreModule().size() > curModIdx) {
            Module module = moduleManager.getPreModule().get(curModIdx);
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
        ModuleManager moduleManager = robot.getModuleManager();
        if (moduleManager.getPreModule().size() <= curModIdx) {
            return null;
        }
        Module module = moduleManager.getPreModule().get(curModIdx);
        return module.getNextEvent(robotContext);
    }
}
