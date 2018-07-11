package com.example.controller.shiroConfig;

import com.example.domain.User;
import com.example.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/11 09:45
 * @PACKAGE_NAME: com.example.controller.shiroConfig
 * @Description: 自定义realm程序
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     *
     * 功能描述:执行授权的逻辑
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/11 9:49
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权的逻辑");
        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        //添加资源的授权字符串
        info.addStringPermission(user.getPerms());
        return info;
    }

    /**
     *
     * 功能描述:执行认证的逻辑
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/11 9:49
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        System.out.println("执行认证的逻辑");
        //编写shiro判断逻辑，判断用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByName(token.getUsername());
        if (null==user){
            //用户名不存在
            return null;
        }
        //判断密码
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
