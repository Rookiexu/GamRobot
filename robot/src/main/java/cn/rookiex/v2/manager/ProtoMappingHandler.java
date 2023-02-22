package cn.rookiex.v2.manager;

import cn.rookiex.v2.handler.LogicHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rookieX 2023/2/22
 */
public class ProtoMappingHandler {

    private static List<Class<?>> HANDLERS = new ArrayList<Class<?>>();

    public static void addLogicHandler(Class<? extends LogicHandler> handlerClass) {
        HANDLERS.add(handlerClass);
    }
}
