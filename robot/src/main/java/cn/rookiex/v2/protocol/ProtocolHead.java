package cn.rookiex.v2.protocol;

import java.nio.ByteBuffer;

/**
 * 分布式情况下,请求头需要传递
 * @author rookieX 2023/2/21
 */
public class ProtocolHead {

    public static int headSize = MyProtocol.bodyIndex;

    ByteBuffer byteBuffer;

    public ProtocolHead(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * 版本号
     */
    public short getVersion() {
        return byteBuffer.get(MyProtocol.versionIndex);
    }

    public void setVersion(short version) {
        byteBuffer.putShort(MyProtocol.versionIndex, version);
    }

    /**
     * 加密状态
     */
    public boolean getEncrypt() {
        return byteBuffer.get(MyProtocol.encryptIndex) != 0;
    }

    public void setEncrypt(boolean isEncrypt) {
        byteBuffer.put(MyProtocol.encryptIndex, (byte) (isEncrypt ? 1 : 0));
    }

    /**
     * 长度
     */
    public int getLength() {
        return byteBuffer.getInt(MyProtocol.lengthIndex);
    }

    public void setLength(int length) {
        byteBuffer.putInt(MyProtocol.lengthIndex, length);
    }

    /**
     * 命令
     */
    public short getCmd() {
        return byteBuffer.getShort(MyProtocol.cmdIndex);
    }

    public void setCmd(short cmd) {
        byteBuffer.putShort(MyProtocol.cmdIndex, cmd);
    }

    /**
     * Session
     */
    public long getSession() {
        return byteBuffer.getLong(MyProtocol.sessionIndex);
    }

    public void setSession(long session) {
        byteBuffer.putLong(MyProtocol.sessionIndex, session);
    }
}
