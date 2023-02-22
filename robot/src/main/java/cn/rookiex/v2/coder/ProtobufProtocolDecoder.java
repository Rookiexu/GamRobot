package cn.rookiex.v2.coder;

import cn.rookiex.v2.protocol.MyProtocol;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProtobufProtocolDecoder implements ProtocolDecoder<Message> {

    private static final ConcurrentMap<Class<?>, Method> methodCache = new ConcurrentHashMap<>();

    private final ExtensionRegistry extensionRegistry;

    public ProtobufProtocolDecoder() {
        this.extensionRegistry = ExtensionRegistry.newInstance();
    }

    @Override
    public Message decode(ByteBuffer byteBuffer, Parameter bodyParameter) {
        try {
            if (Objects.isNull(bodyParameter)) {
                return null;
            }
            Message.Builder builder = getMessageBuilder(bodyParameter.getType());
            byteBuffer.position(MyProtocol.bodyIndex);
            builder.mergeFrom(CodedInputStream.newInstance(byteBuffer), this.extensionRegistry);
            return builder.build();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            byteBuffer.position(0);
        }
    }

    /**
     * Create a new {@code Message.Builder} instance for the given class.
     * <p>This method uses a ConcurrentHashMap for caching method lookups.
     */
    private static Message.Builder getMessageBuilder(Class<?> clazz) throws Exception {
        Method method = methodCache.get(clazz);
        if (method == null) {
            method = clazz.getMethod("newBuilder");
            methodCache.put(clazz, method);
        }
        return (Message.Builder) method.invoke(clazz);
    }
}
