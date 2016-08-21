package com.dounine.corgi.sso.session;

import com.dounine.corgi.sso.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public final class UserSession {

    private static final Logger CONSOLE = LoggerFactory.getLogger(UserSession.class);
    private static final Map<String,User> SESSIONS = new ConcurrentHashMap<>(0);
    private static final RuntimeException TOKEN_NOT_NULL = new RuntimeException("token令牌不能为空");

    private UserSession(){}

    static {
        CONSOLE.info("sessionQuartz start");
        new Thread(new SessionQuartz(SESSIONS)).start();
    }

    /**
     * 新增用户会话信息
     * @param token 令牌值
     * @param user 登录用户信息
     * @return 是否已经登录
     */
    public static void put(String token,User user){
        if(StringUtils.isNotBlank(token)){
            user.setAccessTime(LocalDateTime.now());
            SESSIONS.put(token,user);
        }else{
            throw TOKEN_NOT_NULL;
        }
    }

    /**
     * 根据令牌删除用户会话信息
     * @param token 登录令牌
     * @return 是否删除成功
     */
    public static void remove(String token){
        if(StringUtils.isNotBlank(token)){
            SESSIONS.remove(token);
        }
        throw TOKEN_NOT_NULL;
    }

    public static void removeByUsername(String username){
        if(StringUtils.isNotBlank(username)){
            for(Map.Entry<String,User> entry : SESSIONS.entrySet()){
                if(username.equals(entry.getValue().getUsername())){
                    SESSIONS.remove(entry.getKey());
                    break;
                }
            }
        }else{
            throw new RuntimeException("username用户名不能为空");
        }
    }

    /**
     * 检测用户会话是否存在
     * @param token 令牌
     * @return 是否存在
     */
    public static boolean exist(String token){
        if(StringUtils.isNotBlank(token)){
            return SESSIONS.get(token)!=null;
        }
        throw TOKEN_NOT_NULL;
    }

    /**
     * 获取用户会话总数
     * @return 总数
     */
    public static long count(){
        return SESSIONS.size();
    }

    /**
     * 获取全部用户会话信息
     * @return 会话信息集合
     */
    public static Map<String,User> sessions(){
        if(null!=SESSIONS&&SESSIONS.size()>0){
            return SESSIONS;
        }
        return null;
    }

    public static boolean verify(String token) {
        return SESSIONS.get(token)!=null;
    }

    public static void main(String[] args){
        //System.out.println(TokenUtils.verify(TokenUtils.create("119.129.208.1","admin")));
    }


}
