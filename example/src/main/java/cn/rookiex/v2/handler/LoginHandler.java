package cn.rookiex.v2.handler;

import cn.rookiex.protobuf.protocol.User;
import cn.rookiex.v2.mapping.*;
import cn.rookiex.v2.mapping.proto.ProtoController;
import cn.rookiex.v2.mapping.proto.ProtoReq;
import cn.rookiex.v2.mapping.proto.ProtoResp;
import com.google.protobuf.Message;
import lombok.extern.log4j.Log4j2;

/**
 * @author rookieX 2023/2/22
 */
@ProtoController
@Log4j2
public class LoginHandler {

    @ProtoResp(cmd = (short) User.protocol_id.login_msg_VALUE)
    public void respLogin(@Header SimpleHeader head, @Body User.c2s_Login login){
        long session = head.getSession();
        //getPlayerBySessionId();
        String account = login.getAccount();
        String password = login.getPassword();
        log.info(account + " :: " + password);
    }

    @ProtoReq(cmd = (short) User.protocol_id.login_msg_VALUE)
    public Message reqLogin(String account, String password){
        User.c2s_Login.Builder builder = User.c2s_Login.newBuilder();
        builder.setAccount(account).setPassword(password);
        return builder.build();
    }

    @ProtoReq(cmd = (short) User.protocol_id.login_msg_VALUE)
    public Message reqLogin2(String account, String password){
        User.c2s_Login.Builder builder = User.c2s_Login.newBuilder();
        builder.setAccount(account).setPassword(password);
        return builder.build();
    }
}
