package geekTime.designPatten.authApi;

import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  封装请求参数对象
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
@Data
public class ApiRequest {

    private String  baseUrl;
    private String token;
    private String appId;
    private long timeStamp;

    public ApiRequest() {
    }

    public  ApiRequest(String baseUrl, String token, String appId, long timeStamp) {
       this.baseUrl =baseUrl;
       this.token = token;
       this.appId = appId;
       this.timeStamp = timeStamp;
    }

    public static ApiRequest createFromFullUrl(String url) {
            String[] substr = url.split("\\?");
            String baseUrl = substr[0]+"?";

            String appId = getParamByUrl(url,"appId");
            String token = getParamByUrl(url,"token");
            long timestamp = Long.parseLong(getParamByUrl(url,"timestamp"));

            return  new ApiRequest(baseUrl,token,appId,timestamp);
    }
    /**
     * 获取指定url中的某个参数
     * @param url
     * @param name
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(url);
        if (m.find( )) {
//            System.out.println(m.group(0));
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }



}
