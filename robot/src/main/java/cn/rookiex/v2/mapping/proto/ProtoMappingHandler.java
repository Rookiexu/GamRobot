package cn.rookiex.v2.mapping.proto;

import cn.rookiex.v2.handler.LogicHandler;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rookieX 2023/2/22
 */
@Log4j2
public class ProtoMappingHandler {

    private static List<Class<?>> HANDLERS = new ArrayList<>();

    public static void addLogicHandler(Class<?> handlerClass) {
        HANDLERS.add(handlerClass);
    }

    private Map<Short, ProtoMethod> respMap = Maps.newHashMap();

    private Map<String, ProtoMethod> reqMap = Maps.newHashMap();

    public void initMethod(){
        for (Class<?> c : HANDLERS) {
            if (!c.isAnnotationPresent(ProtoController.class)) {
                log.error(c + ": no anotation");
                continue;
            }
            Object bean = null;
            try {
                bean = c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Method[] methods = c.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(ProtoResp.class)) {
                    ProtoResp annotation = m.getDeclaredAnnotation(ProtoResp.class);
                    registerRespMethod(bean, m, annotation.cmd());
                }
                if (m.isAnnotationPresent(ProtoReq.class)) {
                    ProtoReq annotation = m.getDeclaredAnnotation(ProtoReq.class);
                    registerReqMethod(bean, c, m, annotation.cmd());
                }
            }
        }
    }

    public void registerReqMethod(Object bean, Class<?> c, Method m, short cmd) {
        String reqName = getReqName(c, m);
        ProtoMethod protoMethod = new ProtoMethod(bean, m);
        protoMethod.setCmd(cmd);
        reqMap.put(reqName, protoMethod);
    }

    public void registerRespMethod(Object bean, Method m, short cmd) {
        respMap.put(cmd, new ProtoMethod(bean, m));
    }

    public String getReqName(Class<?> c, Method e){
        String simpleName = c.getSimpleName();
        String name = e.getName();
        return simpleName + "::" + name;
    }

    public ProtoMethod getRespMethod(short cmd) {
        return respMap.get(cmd);
    }

    public ProtoMethod getReqMethod(String cmd) {
        return reqMap.get(cmd);
    }
}
