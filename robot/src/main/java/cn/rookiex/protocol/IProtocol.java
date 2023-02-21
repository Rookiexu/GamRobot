package cn.rookiex.protocol;

/**
 * @author rookieX 2023/2/21
 */
public interface IProtocol {

    byte VERSION = 0x1;

    boolean IS_ENCRYPT = false;

    ProtocolHead getHead();

    ProtocolBody getBody();
}
