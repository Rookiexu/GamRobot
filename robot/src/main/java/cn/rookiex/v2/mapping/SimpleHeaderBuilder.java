package cn.rookiex.v2.mapping;

import cn.rookiex.v2.protocol.ProtocolHead;

/**
 * @author rookieX 2023/2/22
 */
public class SimpleHeaderBuilder<T> implements HeaderBuilder<SimpleHeader> {
    @Override
    public SimpleHeader buildHeader(ProtocolHead head){
        return  SimpleHeader.builder()
                .cmd(head.getCmd())
                .session(head.getSession()).build();
    }
}
