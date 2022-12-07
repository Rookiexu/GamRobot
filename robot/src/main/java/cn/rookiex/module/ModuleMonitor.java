package cn.rookiex.module;

/**
 * 模块状态监听类
 *
 * @author rookieX 2022/12/5
 */
public class ModuleMonitor {

    /**
     * 当前事件队列
     */
    private String currentQue;

    /**
     * 当前执行事件
     */
    private String currentEvent;

    /**
     * 前置事件执行时间
     */
    private long preCostTime;


    /**
     * 前置事件执行时间
     */
    private long eventCostTime;
}
