package corgi.sso.session;

import corgi.sso.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by huanghuanlai on 16/6/13.
 */
public final class TokenUtils {
    private static final int TOKEN_LEN = 4;
    private static final int UUID_SIZE = 12;
    private static final int TIME_SIZE = 13;
    private static final int UUID_INDEX = 1;
    private static final int TIME_INDEX = 3;
    private static final String POINT = ".";

    private TokenUtils(){}

    /**
     * 根据IP地址与用户名生成token令牌
     * @param ip 地扯
     * @param username 用户名
     * @return token令牌(ip反转.uuid后.用户名哈希.生成时间)
     */
    public static String create(String ip,String username){
        long lip = IpUtils.ipToLong(ip);
        String uuid = UUID.randomUUID().toString();
        StringBuffer token = new StringBuffer(uuid.replace("-",""));
        token.append(POINT);
        token.append(String.valueOf(lip));
        token.append(POINT);
        token.append(System.currentTimeMillis());//访问时间
        return token.toString();
    }

    public static boolean verify(String token) {
        if(StringUtils.isBlank(token)){
            return false;
        }
        String[] tokens = token.split("\\.");
        boolean valid = true;
        if(tokens.length!=TOKEN_LEN){
            valid = false;
        }else if(tokens[UUID_INDEX].length()!=UUID_SIZE){//uuid 长度
            valid = false;
        }else if(tokens[TIME_INDEX].length()!=TIME_SIZE){//时间长度
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }
}
