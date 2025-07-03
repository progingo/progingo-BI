package org.progingo.progingobi.config;

import jakarta.servlet.Filter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.progingo.progingobi.auth.JWTRealm;
import org.progingo.progingobi.filter.MyFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(JWTRealm myRealm){
        DefaultWebSecurityManager defaultWebSecurityManager= new DefaultWebSecurityManager();

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);

        defaultWebSecurityManager.setRealm(myRealm);

        //返回
        return defaultWebSecurityManager;
    }

    // 配置过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        //创建过滤器工厂
        ShiroFilterFactoryBean filterFactory=new ShiroFilterFactoryBean();

        //过滤器工厂设置SecurityManager
        filterFactory.setSecurityManager(securityManager);

        HashMap<String, Filter> filters = new HashMap<>();
        filters.put("authc", new MyFilter());
        filterFactory.setFilters(filters);

        //设置shiro的拦截规则
        Map<String,String> filterMap=new HashMap<>();
        // 放行登录和不用登录就可以访问的接口
        filterMap.put("/user/sign/**","anon");
        filterMap.put("/user/login/**","anon");
        // 其余资源都需要用户认证
        filterMap.put("/**","authc");

        //将拦截规则设置给过滤器工厂
        filterFactory.setFilterChainDefinitionMap(filterMap);

        //登录页面
        filterFactory.setLoginUrl("/login");
        return filterFactory;
    }

    // 开启shiro注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    // 开启aop注解支持
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
}
