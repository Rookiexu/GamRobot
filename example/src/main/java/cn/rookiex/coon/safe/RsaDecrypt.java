package cn.rookiex.coon.safe;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.rookiex.coon.safe.key.RsaSecretKey;
import cn.rookiex.coon.safe.key.SecretKey;

/**
 * @author rookieX 2023/2/17
 */
public class RsaDecrypt implements Decrypt {

    private static final RsaDecrypt defaultRsa = new RsaDecrypt();

    public static RsaDecrypt getDefault() {
        return defaultRsa;
    }

    private RSA rsa;

    @Override
    public byte[] decrypt(byte[] bytes) {
        return rsa.decrypt(bytes, KeyType.PrivateKey);
    }

    @Override
    public void setSecretKey(SecretKey key) {
        RsaSecretKey rsaSecretKey = (RsaSecretKey) key;
        this.rsa = SecureUtil.rsa(rsaSecretKey.getKey(SecretKey.PRIVATE), null);
    }

    @Override
    public void setSecretKey(String key) {
        this.rsa = SecureUtil.rsa(key, null);
    }

    @Override
    public void setSecretKey(byte[] key) {
        this.rsa = SecureUtil.rsa(key, null);
    }

}
