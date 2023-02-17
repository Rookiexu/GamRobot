package cn.rookiex.coon.safe;

import cn.rookiex.coon.safe.key.SecretKey;

/**
 * @author rookieX 2023/2/17
 */
public interface Decrypt {
    /**
     * @param bytes  密文
     * @return  原始数据
     */
    byte[] decrypt(byte[] bytes);

    void setSecretKey(SecretKey key);

    void setSecretKey(String key);

    void setSecretKey(byte[] key);
}
