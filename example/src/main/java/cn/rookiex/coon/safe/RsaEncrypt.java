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

    @Override
    public byte[] encrypt(byte[] bytes) {
        return rsa.encrypt(bytes, KeyType.PublicKey);
    }

    @Override
    public void setSecretKey(SecretKey key) {
        RsaSecretKey rsaSecretKey = (RsaSecretKey) key;
        this.rsa = SecureUtil.rsa(null,rsaSecretKey.getPublicKey());
    }

}
