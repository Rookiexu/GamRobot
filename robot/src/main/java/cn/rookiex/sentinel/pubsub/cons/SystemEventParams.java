package cn.rookiex.sentinel.pubsub.cons;

/**
 * @author rookieX 2022/12/10
 */
public interface SystemEventParams {
    /**
     * 执行线程id
     */
    String PROCESSOR_ID = "processId";

    /**
     * ROBOT_ID
     */
    String ROBOT_ID = "robotId";

    /**
     * 等待消息id
     */
    String WAIT_RESP_ID = "respId";

    /**
     * REQ消息id
     */
    String REQ_MSG_ID = "reqId";


    /**
     * REQ消息id
     */
    String REQ_MSG_NAME = "reqName";

    /**
     * 是否跳过响应等待
     */
    String IS_SKIP_RESP = "skip";


    /**
     * 当前毫秒时
     */
    String CUR_MS = "curMs";

    /**
     * 消息返回时间
     */
    String RESP_TIME = "respTime";

    /**
     * 消息收到消耗时间
     */
    String RESP_COST = "respCost";

    /**
     * 返回消息處理完成消耗时间2
     */
    String RESP_DEAL_COST = "respDealCost";
}
