package cn.rookiex.v2.mapping.proto;

import cn.rookiex.v2.mapping.MethodParameter;
import com.sun.istack.internal.Nullable;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author rookieX 2023/2/22
 */
@Log4j2
public class ProtoMethod {

    private final Object bean;

    private final Class<?> beanType;

    private final Method method;

    private final Parameter[] parameters;

    private short cmd;

    @Nullable
    private volatile List<Annotation[][]> interfaceParameterAnnotations;

    public ProtoMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanType = bean.getClass();
        this.method = method;
        this.parameters = method.getParameters();
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public short getCmd() {
        return cmd;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Object getBean() {
        return bean;
    }
}
