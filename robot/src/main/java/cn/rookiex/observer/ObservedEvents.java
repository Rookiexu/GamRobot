package cn.rookiex.observer;

/**
 * @author rookieX 2022/12/10
 */
public interface ObservedEvents {

    /**
     * 新增连接
     */
    String INCR_COON = "incrCoon";

    /**
     * 新增登录
     */
    String INCR_LOGIN = "incrLogin";

    /**
     * 新增发消息
     */
    String INCR_SEND = "incrSend";

    /**
     * 新增消息接收
     */
    String INCR_RESP = "incrResp";

    /**
     * 新增消息接收处理完成
     */
    String INCR_RESP_DEAL = "incrRespDeal";

    /**
     * 新增robot
     */
    String INCR_ROBOT = "incrRobot";
}
