package cn.rookiex.v2.coder;


import cn.rookiex.v2.protocol.IProtocol;

import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;

/**
 * 协议解析器
 *
 * @param <T>
 */
public interface ProtocolDecoder<T> {


    T decode(IProtocol protocol, ByteBuffer byteBuffer, Parameter bodyParameter);

}
