package geekTime.designPatten.authApi;

/**
 * 凭证存储抽象接口
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
public interface CredentialStorage {

    String getPasswordByAppId(String appId);
}
