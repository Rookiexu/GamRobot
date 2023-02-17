package cn.rookiex.coon.safe.key;

import lombok.Getter;

/**
 * @author rookieX 2023/2/17
 */
@Getter
public class RsaSecretKey implements SecretKey {

    private RsaSecretKey(){};

    public static final RsaSecretKey key = new RsaSecretKey();

    public static RsaSecretKey getInstance() {
        return key;
    }

    private final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALYIMKQkZk2J6Nqn3IduFgGEF7QJGlnWSl7w7eqdIL8+I2vfF81ioz4WIZE+ABd8BeC3s+YlFTjvqQZplWTUTPy2uPolWF+cPgUMPmYi65FvaFr1z9pnGeGYQ9tkc9gOLZE5PE8qh3ZdNDf0MYS4wwNF66PZXO+tbnw/54npKiUnAgMBAAECgYEAhGceojXLp7iZA3o3gNaE8SNYbl50qUWiIL6T/yO9Lv2OnCbQVFbbsAw+7Dsxq+NNDy/vjYYJkrlXs09XF9kICUn1PMbMzsILluwvEN2vNj13ZrW4qua/VURuUqjBpWDz7NGVH1lRmsvMYx2IvzDVkE1yoj5OrdCkHwHiB34Q4HkCQQDqPtc6NEMeTVpzGm8Pzd2K4HkroOkZRsHSIRuErxAfcxcHT3x02G5yumq+cvZG4mQnZoqlfv5yGOZFBWgSlrlNAkEAxu/60rrHgFa6+qJZ3+i+9RbtFp+GC3j6IQPpkbbROxUpnYVMhJqr1r0b3sAjBK1+tCC/2yufvtgdUC+f4rM+QwJAC/WdY3lbm+ZzIcFNvk7Sas6/IP0cKZA1QRczoniVqCVmHZdvUT/jk7P00zhnXoMcfe58UarELusahpeaEqjjWQJAYeEvQsGdaPrDUo/Qsoc8dTBLhKcGd9yVDOt03znY5j36VMOCK3hCdgxjiwxz7lhCj/PREc/78BU7Dvnvq8XPKwJAHCFkyJ+RL4cTOKZ+h1cYqXfktDi3/y4+piApiUxNMO+JuFsrKnOcbsml9fnxbOAZN0Q+e+LxHriVDTxWdQq2Tw==\n";

    private final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2CDCkJGZNiejap9yHbhYBhBe0CRpZ1kpe8O3qnSC/PiNr3xfNYqM+FiGRPgAXfAXgt7PmJRU476kGaZVk1Ez8trj6JVhfnD4FDD5mIuuRb2ha9c/aZxnhmEPbZHPYDi2ROTxPKod2XTQ39DGEuMMDReuj2VzvrW58P+eJ6SolJwIDAQAB\n";
}
