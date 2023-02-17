package cn.rookiex.coon.safe.key;

import lombok.Data;

/**
 * @author rookieX 2023/2/17
 */
@Data
public class AesSecretKey implements SecretKey {

    /**
     * 给客户端的文件需要屏蔽私钥,example只是模拟,所以都放进去了
     */
    private String onlyKey;

    public AesSecretKey(String onlyKey) {
        this.onlyKey = onlyKey;
    }

    private AesSecretKey() {
    }

    public static final AesSecretKey key = new AesSecretKey();

    public static AesSecretKey getDefault() {
        return key;
    }

    @Override
    public String getKey(int type) {
        switch (type) {
            case SecretKey.SAME:
                return getOnlyKey();
            default:
                return null;
        }
    }
}
