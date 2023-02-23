package cn.rookiex.v2.queue;

import cn.rookiex.v2.mapping.proto.ProtoMessageHandler;
import cn.rookiex.v2.protocol.IProtocol;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * @author rookieX 2023/2/22
 */
@Log4j2
public class ProtocolQueue {

    private ExecutorService pool;

    ProtoMessageHandler messageHandler;

    private final Queue<IProtocol> queue = new LinkedList<>();

    private String name;

    public ProtocolQueue(String name, ExecutorService pool){
        this.name = name;
        this.pool = pool;
    }

    public String getName() {
        return name;
    }

    public void setMessageHandler(ProtoMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * 同时只执行一个任务
     * @param protocol
     */
    public void enqueue(IProtocol protocol) {
        boolean canExec = false;
        synchronized (queue) {
            queue.add(protocol);
            if (queue.size() == 1) {
                canExec = true;
            }
        }

        if (canExec) {
            execute(protocol);
        }
    }

    public void dequeue(IProtocol protocol) {
        IProtocol nextAction = null;
        synchronized (queue) {
            IProtocol temp = queue.remove();
            if (queue.size() != 0) {
                nextAction = queue.peek();
            }
            if (temp != protocol) {
                log.error("action queue " + name + " error. temp " + temp.toString() + ", action : " + protocol.toString());
            }
        }

        if (nextAction != null) {
            execute(nextAction);
        }
    }

    public void execute(IProtocol protocol) {
        pool.execute(()->{
            try {
                messageHandler.handle(protocol);
            }catch (Exception e){
                log.error(e, e);
            }finally {
                dequeue(protocol);
            }
        });
    }

}
