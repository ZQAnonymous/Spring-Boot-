package com.crmeeting.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 21:04
 * @PACKAGE_NAME: com.crmeeting.config
 * @Description:
 */
@Configuration
public class ShiroConfig {
    /**
     *
     * 功能描述:创建ShiroFilterFactoryBean
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/11 9:55
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> filterMap = new LinkedHashMap<>();

        //放行login与testThymeleaf请求
        filterMap.put("/testThymeleaf","anon");
        filterMap.put("/login","anon");
        filterMap.put("/doRegister","anon");
        filterMap.put("/register","anon");
        filterMap.put("/index","anon");

        //授权过滤器
        //注意：当前授权拦截后，shiro会自动跳转到未授权界面
        filterMap.put("/add","perms[user:add]");
        filterMap.put("/update","perms[user:update]");
        //拦截所有请求
        filterMap.put("/*","authc");
        //修改调整后的登录界面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //调整未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //shiro内置过滤器可以实现相关的拦截
        return shiroFilterFactoryBean;
    }


    /**
     *
     * 功能描述:创建DefaultWebSecurityManager
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/11 9:52
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            @Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getRealm(matcher));
        return securityManager;
    }

    /**
     *
     * 功能描述:创建Reaml
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/11 9:51
     */
//    @Bean(name = "userRealm")
//    public UserRealm getRealm(){
//        return new UserRealm();
//    }
    @Bean(name = "userRealm")
    public UserRealm getRealm(HashedCredentialsMatcher matcher){
        UserRealm myShiroRealm = new UserRealm();
        myShiroRealm.setCredentialsMatcher(matcher);
        return myShiroRealm;
    }

    /**
     * 密码匹配凭证管理器
     *
     * @return
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 采用MD5方式加密
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 设置加密次数
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }


    /*
    * 配置ShiroDialect,用于thymeleaf与shiro标签配合使用
    */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
