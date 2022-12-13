package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.Module;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

import java.util.List;

/**
 * @author rookieX 2022/12/12
 */
public class RandomStage implements ModuleStage {

    ModuleStage stage = new OrderStage();

    @Override
    public void setStage(ModuleStage stage) {
    }

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        if (robot.getCurModStage() == Module.RANDOM) {
            List<Module> randomModules = robot.getRandomModules();
            int curModIdx = robot.getCurModIdx();
            if (randomModules.size() == curModIdx + 1) {
                Module module = randomModules.get(curModIdx);
                return module.isRunOut(robotContext);
            }
        }
        return false;
    }

    @Override
    public boolean isModOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        int curModIdx = robot.getCurModIdx();
        List<Module> randomModules = robot.getRandomModules();

        if (randomModules.size() > curModIdx) {
            Module module = randomModules.get(curModIdx);
            return module.isRunOut(robotContext);
        }
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModStage(Module.ORDER);

        List<Module> randomModules = robot.getRandomModules();

        int curModIdx = robot.getCurModIdx();
        if (randomModules.size() > curModIdx) {
            Module module = randomModules.get(curModIdx);
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
        List<Module> randomModules = robot.getRandomModules();
        if (randomModules.size() <= curModIdx) {
            return null;
        }
        Module module = randomModules.get(curModIdx);
        return module.getNextEvent(robotContext);
    }
}
