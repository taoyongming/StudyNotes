package geekTime.designPatten.authApi;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;

/**
 * DESCRIPTION ：
 * token 对象 充血模型
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
@Data
public class AuthToken {
    
    public static final long DELLAULT_EXPIRE_TIME_INTERVAL  = 1*60*1000;

    private String token;

    private long createTime;

    private long expireTimeInterval = AuthToken.DELLAULT_EXPIRE_TIME_INTERVAL;

    public AuthToken(String token, long createTime, long expireTimeInterval) {
        this.token = token;
        this.createTime = createTime;
        this.expireTimeInterval = expireTimeInterval;
    }

    public AuthToken(String token, long createTime) {
        this.token = token;
        this.createTime = createTime;
    }



    public static AuthToken generate(String originalUrl, String appId, String password, long timestamp) {
        String str = originalUrl+"appId="+appId+"&password="+password+"&timestamp="+timestamp;
        System.out.println("orientUrl:"+str);
        String token = DigestUtils.sha1Hex(str);
        AuthToken authToken = new AuthToken(token,timestamp);
        return authToken;
    }

    public String getToken(){
        return this.token;
    }

    public boolean isExpire() {
        long now = System.currentTimeMillis();
        long time = now -createTime;
        System.out.println("time:"+time);
        boolean isExpire = time > expireTimeInterval ? true :false;

        return isExpire;
    }

    public boolean match(AuthToken authToken) {
        if(authToken.getToken().equals(this.token)) {
            return true;
        }else {
            return false;
        }

    }
}
