package cn.rookiex.coon.safe.key;

import lombok.Data;

/**
 * @author rookieX 2023/2/17
 */
@Data
public class RsaSecretKey implements SecretKey {

    private static String pvKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPqTs0AKB7lacd+Jwy5B5HHlEnz8wntcyk+xmGkvQQd9riru05z+guSIjKrDqaH/gMNqwoJdJupnX+cTjl+agySMgJyeTvqshRwgzWRMPqUU2DI/RdrY3/uMizyN/tSOcukUQLi1I1TsCajNmuEtfWQ8WonBqcSkKCbwHR5o1W/lAgMBAAECgYEAqRd8sa1TEwhuqZtsY83BW/5qIH/suZSfTXIZ2Hkz19s+6efqUxoIuDFRQ3bwBMrHWJe1lIVJ2opR1DyFKgy5i12UJHjivguwTZTpJfpiPHoRBE/HmVC+wS+HXZwVb00eofZZ2/Suz4mk2fsHO+1KEx8XGulwxE6JzxaRJOkXAq0CQQD+STgiKsVKKvEITQUfSgrEuywNWLm1eexbEWHfXE3ohXHVbJBS34k8t2ctqzy4C2KOp7ZLkNuqGMasB1JmCxHTAkEA/EQUrvJSrAKIkAx9pj+H7rTAlqjhq5eNV5iteILpjHPS9zC3I4GiP1+qNS6Nj9jYF6bDOhs3I2PN/fvsy8ssZwJBAK+AeW2bd28pFBJWliJyR9xgEpdjlUjbTXXNGW+/lUXWVCqpMzL/dQiizGOIu++KbAtpVTfyUqmjT/uLUiN7oGECQQCp54C2w+cCi0JHIuKN0kRPddYwotY7p/s+gEp13FZIjsoMSvdOJsWMQXZ2E0CtHbONWngq7qGYaGQZKFA4gkMJAkATDr013IjkvqU/l8hh5pPavgXqA+AiCSZnD/ZeUyRxZ8akpEb18NGOvgT2lENMGE1r+UtuSXIufTPSxuEysH5Z";
    private static String pbKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD6k7NACge5WnHficMuQeRx5RJ8/MJ7XMpPsZhpL0EHfa4q7tOc/oLkiIyqw6mh/4DDasKCXSbqZ1/nE45fmoMkjICcnk76rIUcIM1kTD6lFNgyP0Xa2N/7jIs8jf7UjnLpFEC4tSNU7AmozZrhLX1kPFqJwanEpCgm8B0eaNVv5QIDAQAB";

    /**
     * 给客户端的文件需要屏蔽私钥,example只是模拟,所以都放进去了
     */
    private String privateKey;
    private String publicKey;

    public RsaSecretKey() {
    }

    private RsaSecretKey(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public static final RsaSecretKey key = new RsaSecretKey(pvKey, pbKey);

    public static RsaSecretKey getDefault() {
        return key;
    }

    @Override
    public String getKey(int type) {
        switch (type) {
            case SecretKey.PUBLIC:
                return getPublicKey();
            case SecretKey.PRIVATE:
                return getPrivateKey();
            default:
                return null;
        }
    }
}
