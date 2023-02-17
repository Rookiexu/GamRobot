package cn.rookiex.coon.safe.key;

/**
 * @author rookieX 2023/2/17
 */
public interface SecretKey {

    int PRIVATE = 1,PUBLIC = 2, SAME = 3;

    String getKey(int type);
}
