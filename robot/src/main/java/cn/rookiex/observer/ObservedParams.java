package cn.rookiex.observer;

/**
 * @author rookieX 2022/12/10
 */
public interface ObservedParams {
    /**
     * 执行线程id
     */
    String PROCESSOR_ID = "processId";

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
}
