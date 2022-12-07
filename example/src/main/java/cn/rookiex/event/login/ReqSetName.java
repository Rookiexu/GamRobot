package cn.rookiex.event.login;

import cn.rookiex.event.ReqEvent;

/**
 * @author rookieX 2022/12/6
 */
public class ReqSetName implements ReqEvent {
    @Override
    public int getRespEventId() {
        return 1;
    }
}
