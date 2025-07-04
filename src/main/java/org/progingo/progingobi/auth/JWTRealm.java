package org.progingo.progingobi.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.progingo.progingobi.domain.dto.UserDTO;
import org.progingo.progingobi.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //用于鉴权的方法
        UserDTO user = (UserDTO)principals.getPrimaryPrincipal();

        if (user == null || user.getId() == null){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addRole("tourist");
            return simpleAuthorizationInfo;
        }


        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        simpleAuthorizationInfo.addRole("user");

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();//传递的参数类型是MyToken，而我们重写了其中的两个方法，所以能获取到token
        System.out.println(token);

        if (token == null || token.isEmpty()){//如果没有token就以游客身份记录
            System.out.println("游客身份");
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    UserDTO.builder().id(null).build(),
                    token,
                    this.getName());
            return info;
        }

        UserDTO userCache = tokenUtil.getUserCache(token);//将token传递给工具类，从缓存中查找是否有对应token的用户信息(因为正常用户登录后信息都保存在redis中了，现在就是逆向操作，去redis中查找，如果查找不到自然就说明token是非法或者过期的，查找到了则就是身份校验成功)

        if (userCache == null){//无效token就以游客身份记录
            System.out.println("无效token");
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    UserDTO.builder().id(null).build(),
                    token,
                    this.getName());
            return info;
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                userCache,
                token,
                this.getName());
        return info;
    }
}
