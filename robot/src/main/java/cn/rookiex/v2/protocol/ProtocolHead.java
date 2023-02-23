package cn.rookiex.v2.protocol;

/**
 * @author rookieX 2023/2/22
 */
public interface ProtocolHead {
    short getCmd();

    void setCmd(short cmd);

    long getSession();

    void setSession(long sessionId);
}
