package cn.rookiex.v2.mapping.proto;

import cn.hutool.core.util.ObjectUtil;
import cn.rookiex.v2.coder.ProtobufProtocolDecoder;
import cn.rookiex.v2.coder.ProtocolDecoder;
import cn.rookiex.v2.mapping.*;
import cn.rookiex.v2.protocol.MyProtocol;
import cn.rookiex.v2.protocol.ProtocolHead;
import com.google.protobuf.Message;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author rookieX 2023/2/22
 */
@Log4j2
public class ProtoMessageHandler {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private ProtocolDecoder<Message> decoder;

    private HeaderBuilder<SimpleHeader> headerBuilder = new SimpleHeaderBuilder<>();

    ProtoMappingHandler protoMappingHandler;

    public ProtoMessageHandler(ProtoMappingHandler protobufMappingHandler, ProtobufProtocolDecoder protocolDecoder) {
        this.protoMappingHandler = protobufMappingHandler;
        this.protoMappingHandler.initMethod();
        this.decoder = protocolDecoder;
    }

    public ProtoMessageHandler(ProtoMappingHandler protobufMappingHandler, ProtobufProtocolDecoder protocolDecoder, HeaderBuilder<SimpleHeader> headerBuilder) {
        this.protoMappingHandler = protobufMappingHandler;
        this.protoMappingHandler.initMethod();
        this.decoder = protocolDecoder;
        this.headerBuilder = headerBuilder;
    }

    public void handle(ByteBuffer byteBuffer) {

        MyProtocol protocol = MyProtocol.create(byteBuffer);

        //找到调用方法
        ProtoMethod protobufMethod = protoMappingHandler.getRespMethod(protocol.getHead().getCmd());
        if (Objects.isNull(protobufMethod)) {
            throw new RuntimeException("no method");
        }

        log.info("handle command:" + protocol.getHead().getCmd());

        Parameter bodyParameter = null;
        Parameter[] methodParameters = protobufMethod.getParameters();
        if (Objects.isNull(methodParameters)) {
            throw new RuntimeException("no method parameters");
        }

        for (Parameter methodParameter : methodParameters) {
            Body[] annotationsByType = methodParameter.getAnnotationsByType(Body.class);
            if (annotationsByType != null && annotationsByType.length > 0) {
                bodyParameter = methodParameter;
            }
        }

        Message message = decoder.decode(byteBuffer, bodyParameter);
        Object[] args = getMethodArgumentValues(protobufMethod, protocol.getHead(), message);

        try {
            protobufMethod.getMethod().invoke(protobufMethod.getBean(), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }



    private Object[] getMethodArgumentValues(ProtoMethod protobufMethod, ProtocolHead header, Message message) {
        Parameter[] parameters = protobufMethod.getParameters();
        if (ObjectUtil.isEmpty(parameters)) {
            return EMPTY_ARGS;
        }
        SimpleHeader simpleHeader = headerBuilder.buildHeader(header);

        List<Object> args = new ArrayList<>(parameters.length);
        for (Parameter parameter : parameters) {
            Annotation[] annotations = parameter.getAnnotations();
            Stream.of(annotations).forEach(annotation -> {
                if (annotation instanceof Header) {
                    args.add(simpleHeader);
                } else if (annotation instanceof Body) {
                    args.add(message);
                }
            });
        }
        return args.toArray();
    }

}
