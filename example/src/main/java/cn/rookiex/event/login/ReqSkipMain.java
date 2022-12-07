package cn.rookiex.event.login;

import cn.rookiex.event.ReqEvent;

/**
 * 登录事件
 *
 * @author rookieX 2022/12/6
 */
public class ReqSkipMain implements ReqEvent {

    public int getRespEventId() {
        return 2;
    }
}
