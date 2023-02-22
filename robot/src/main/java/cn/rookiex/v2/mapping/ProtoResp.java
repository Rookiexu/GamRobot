package cn.rookiex.v2.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoResp {
    /**
     * 命令
     *
     * @return 命令号
     */
    short cmd();
}
