package com.crmeeting.config;

import com.crmeeting.entity.User;
import com.crmeeting.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 21:06
 * @PACKAGE_NAME: com.crmeeting.config
 * @Description:
 */
public class UserRealm extends AuthorizingRealm{

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
        System.out.println(subject.getPrincipal().toString());
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
        if (null != user){
            Object principal =  user.getUsername();
            Object credentials = user.getPassword();
            ByteSource salt = ByteSource.Util.bytes(principal);
            String realmName = this.getName();
            //判断密码
            return new SimpleAuthenticationInfo(user, credentials, salt,realmName);
        }else{
            return null;
        }
    }
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
//            throws AuthenticationException {
//        System.out.println("执行认证的逻辑");
//        //编写shiro判断逻辑，判断用户名和密码
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        User user = userService.findByName(token.getUsername());
//        if (null==user){
//            //用户名不存在
//            return null;
//        }
//        //判断密码
//        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
//    }
}
