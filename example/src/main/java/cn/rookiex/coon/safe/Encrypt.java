package cn.rookiex.coon.safe;

import cn.rookiex.coon.safe.key.SecretKey;

/**
 * @author rookieX 2023/2/17
 */
public interface Encrypt {
    /**
     * @param bytes 被加密数据
     * @return 密文
     */
    byte[] encrypt(byte[] bytes);

    void setSecretKey(SecretKey key);

    SecretKey getSecretKey();
}
