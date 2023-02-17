package cn.rookiex.coon.safe;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.rookiex.coon.safe.key.RsaSecretKey;
import cn.rookiex.coon.safe.key.SecretKey;

/**
 * @author rookieX 2023/2/17
 */
public class AesEncrypt implements Encrypt {

    private AES aes;

    private static final AesEncrypt defaultRsa = new AesEncrypt();

    public static AesEncrypt getDefault() {
        return defaultRsa;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        return aes.encrypt(bytes);
    }

    @Override
    public void setSecretKey(SecretKey key) {
        setSecretKey(key.getKey(SecretKey.SAME));
    }

    @Override
    public void setSecretKey(String key) {
        this.aes = SecureUtil.aes(key.getBytes());
    }

    @Override
    public void setSecretKey(byte[] key) {
        this.aes = SecureUtil.aes(key);
    }

}
