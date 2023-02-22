package cn.rookiex.v2.handler;

import cn.rookiex.v2.mapping.proto.ProtoMessageHandler;

public class LogicHandler {

    private ProtoMessageHandler handler;

    public ProtoMessageHandler getHandler() {
        return handler;
    }

    public void setHandler(ProtoMessageHandler handler) {
        this.handler = handler;
    }
}
