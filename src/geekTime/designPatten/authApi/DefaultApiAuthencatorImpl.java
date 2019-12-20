package geekTime.designPatten.authApi;

/**
 * DESCRIPTION ：
 * 认证接口实现类
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
public class DefaultApiAuthencatorImpl implements ApiAuthencator {

        private CredentialStorage credentialStorage;

        public DefaultApiAuthencatorImpl() {
            this.credentialStorage = new MemoryCredentialStorage();
        }

        public DefaultApiAuthencatorImpl(CredentialStorage credentialStorage) {
            this.credentialStorage = credentialStorage;
        }

        @Override
        public void auth(String url) {
            ApiRequest apiRequest = ApiRequest.createFromFullUrl(url);
            System.out.println(apiRequest);
            auth(apiRequest);
        }

        @Override
        public void auth(ApiRequest apiRequest) {
            String appId = apiRequest.getAppId();
            String token = apiRequest.getToken();
            long timestamp = apiRequest.getTimeStamp();
            String originalUrl = apiRequest.getBaseUrl();
            System.out.println("originalUrl"+originalUrl);
            AuthToken clientAuthToken = new AuthToken(token, timestamp);

            System.out.println("clientAuthToken"+clientAuthToken);

            if (clientAuthToken.isExpire()) {
                throw new RuntimeException("Token is expired.");
            }

            String password = credentialStorage.getPasswordByAppId(appId);

            AuthToken serverAuthToken =  AuthToken.generate(originalUrl, appId, password, timestamp);

            System.out.println("serverAuthToken:"+serverAuthToken);

            if (!serverAuthToken.match(clientAuthToken)) {
                throw new RuntimeException("Token verfication failed.");
            }
        }

        public static void main(String[] args) {
            String originalUrl = "http://www.taoyongming.com/test?";
            String appId = "123456";
            String password = "123456";
            long timestamp = System.currentTimeMillis();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setAppId("123456");
            apiRequest.setBaseUrl(originalUrl);
            apiRequest.setTimeStamp(timestamp);

            AuthToken clientAuthToken =  AuthToken.generate(originalUrl, appId, password, timestamp);
            apiRequest.setToken(clientAuthToken.getToken());

            String newUrl = originalUrl+"appId="+appId+"&token="+clientAuthToken.getToken()+"&timestamp="+timestamp;
            System.out.println("newUrl:"+newUrl);

            ApiAuthencator apiAuthencator = new DefaultApiAuthencatorImpl();
            apiAuthencator.auth(newUrl);
        }
}
