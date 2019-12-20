package geekTime.designPatten.authApi;

/**
 * DESCRIPTION: 认证接口
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
public interface ApiAuthencator {
    void auth(String url);
    void auth(ApiRequest apiRequest);
}
