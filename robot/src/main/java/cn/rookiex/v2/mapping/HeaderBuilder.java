package cn.rookiex.v2.mapping;

import cn.rookiex.v2.protocol.ProtocolHead;

/**
 * @author rookieX 2023/2/22
 */
public interface HeaderBuilder<T extends SimpleHeader> {
    T buildHeader(ProtocolHead head);
}
