package cn.rookiex.coon.server.timer;

import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

/**
 * @author rookieX 2023/2/16
 */
public class TimerHolder {
    public static HashedWheelTimer millisTimer;

    public static void init(){
        millisTimer = new HashedWheelTimer(1, TimeUnit.MILLISECONDS, 10);
    }
}
