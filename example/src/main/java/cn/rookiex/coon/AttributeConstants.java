package cn.rookiex.coon;

import io.netty.util.AttributeKey;

/**
 * @author rookieX 2023/2/17
 */
public class AttributeConstants {

    public static final AttributeKey<Boolean> READY = AttributeKey.valueOf("false");

    public static final AttributeKey<String> CLIENT_KEY = AttributeKey.valueOf("clientKey");

    public static final AttributeKey<String> SERVER_KEY = AttributeKey.valueOf("serverKey");

}
