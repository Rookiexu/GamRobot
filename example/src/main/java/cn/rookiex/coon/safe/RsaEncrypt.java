package cn.rookiex.coon.safe;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.rookiex.coon.safe.key.RsaSecretKey;
import cn.rookiex.coon.safe.key.SecretKey;

/**
 * @author rookieX 2023/2/17
 */
public class RsaEncrypt implements Encrypt {

    private RSA rsa;

    private static final RsaEncrypt defaultRsa = new RsaEncrypt();

    public static RsaEncrypt getDefault() {
        return defaultRsa;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        return rsa.encrypt(bytes, KeyType.PublicKey);
    }

    @Override
    public void setSecretKey(SecretKey key) {
        RsaSecretKey rsaSecretKey = (RsaSecretKey) key;
        this.rsa = SecureUtil.rsa(null,rsaSecretKey.getKey(SecretKey.PUBLIC));
    }

    @Override
    public void setSecretKey(String key) {
        this.rsa = SecureUtil.rsa(null,key);
    }

    @Override
    public void setSecretKey(byte[] key) {
        this.rsa = SecureUtil.rsa(null,key);
    }

}
