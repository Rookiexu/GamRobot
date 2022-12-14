package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/12
 */
@Log4j2
public class OrderStage implements ModuleStage {

    private static final ModuleStage stage = new RandomStage();

    @Override
    public void setStage(ModuleStage stage) {
    }

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        if (robot.getCurModStage() == Module.ORDER) {
            ModuleManager moduleManager = robot.getModuleManager();
            List<Module> orderModule = moduleManager.getOrderModule();
            int curModIdx = robot.getCurModIdx();
            if (orderModule.size() == curModIdx + 1) {
                Module module = orderModule.get(curModIdx);
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

        if (orderModule.size() > curModIdx) {
            Module module = orderModule.get(curModIdx);
            return module.isRunOut(robotContext);
        }
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurEventIdx(0);

        ModuleManager moduleManager = robot.getModuleManager();
        List<Module> orderModule = moduleManager.getOrderModule();
        int curModIdx = robot.getCurModIdx();
        Module module = orderModule.get(curModIdx);
        module.initRunEvent(robotContext);
    }

    @Override
    public void initStage(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.setCurModStage(Module.ORDER);
        robot.setCurModIdx(0);

        initMod(robotContext);
    }

    @Override
    public ModuleStage nextStage(RobotContext robotContext) {
        return stage;
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
        List<Module> orderModule = moduleManager.getOrderModule();
        if (orderModule.size() <= curModIdx) {
            return null;
        }
        Module module = orderModule.get(curModIdx);
        return module.getNextEvent(robotContext);
    }
}
