package cn.rookiex.v2.protocol.ro;

import cn.rookiex.v2.protocol.ProtocolHead;

import java.nio.ByteBuffer;

/**
 * 传递请求头可以跨进程处理问题,也避免业务对象耦合代码
 * @author rookieX 2023/2/21
 */
public class RoHead implements ProtocolHead {

    ByteBuffer byteBuffer;

    public RoHead(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * 版本号
     */
    public short getVersion() {
        return byteBuffer.get(RoProtocol.versionIndex);
    }

    public void setVersion(short version) {
        byteBuffer.putShort(RoProtocol.versionIndex, version);
    }

    /**
     * 加密状态
     */
    public boolean getEncrypt() {
        return byteBuffer.get(RoProtocol.encryptIndex) != 0;
    }

    public void setEncrypt(boolean isEncrypt) {
        byteBuffer.put(RoProtocol.encryptIndex, (byte) (isEncrypt ? 1 : 0));
    }

    /**
     * 长度
     */
    public int getLength() {
        return byteBuffer.getInt(RoProtocol.lengthIndex);
    }

    public void setLength(int length) {
        byteBuffer.putInt(RoProtocol.lengthIndex, length);
    }

    /**
     * 命令
     */
    @Override
    public short getCmd() {
        return byteBuffer.getShort(RoProtocol.cmdIndex);
    }

    @Override
    public void setCmd(short cmd) {
        byteBuffer.putShort(RoProtocol.cmdIndex, cmd);
    }

    /**
     * Session
     */
    public long getSession() {
        return byteBuffer.getLong(RoProtocol.sessionIndex);
    }

    public void setSession(long session) {
        byteBuffer.putLong(RoProtocol.sessionIndex, session);
    }
}
