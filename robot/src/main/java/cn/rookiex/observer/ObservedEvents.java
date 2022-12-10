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

    String INCR_SEND = "incrSend";
    String INCR_RESP = "incrResp";
    String INCR_RESP_DEAL = "incrRespDeal";
}
