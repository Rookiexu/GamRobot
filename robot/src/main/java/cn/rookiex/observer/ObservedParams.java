package cn.rookiex.observer;

/**
 * @author rookieX 2022/12/10
 */
public interface ObservedParams {
    /**
     * 执行线程id
     */
    String PROCESSOR_ID = "processorId";

    /**
     * 等待消息id
     */
    String WAIT_RESP_ID = "WAIT_RESP_ID";
    /**
     * 是否跳过响应等待
     */
    String IS_SKIP_RESP = "SKIP_RESP";
}
