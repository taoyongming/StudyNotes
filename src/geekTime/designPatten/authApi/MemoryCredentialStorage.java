package geekTime.designPatten.authApi;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *凭证仓库
 *
 * @author tym
 * @ceeate 2019/12/20
 **/
public class MemoryCredentialStorage implements CredentialStorage {

            Map<String,String> IdPasswordMap = new LinkedHashMap<String, String>();
            public MemoryCredentialStorage() {
                IdPasswordMap.put("id1","p1");
                IdPasswordMap.put("id2","p2");
                IdPasswordMap.put("id3","p3");
                IdPasswordMap.put("id4","p4");
                IdPasswordMap.put("123456","123456");
            }

            @Override
            public String getPasswordByAppId(String appId) {
                return IdPasswordMap.get(appId);
            }

}
