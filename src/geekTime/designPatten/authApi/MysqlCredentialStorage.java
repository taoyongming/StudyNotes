package geekTime.designPatten.authApi;

/**
 *凭证仓库
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
public class MysqlCredentialStorage implements CredentialStorage {

    @Override
    public String getPasswordByAppId(String appId) {
        if(appId.equals("123456")){
            return "123456";
        }
        return "";
    }
}
