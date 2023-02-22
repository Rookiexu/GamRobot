package cn.rookiex.v2.mapping;

import lombok.Builder;
import lombok.Data;

/**
 * protobuf 请求头,包含请求号和请求连接id
 * @author rookieX 2023/2/22
 */
@Data
@Builder
public class SimpleHeader {
    private short cmd;
    private long session;
}
