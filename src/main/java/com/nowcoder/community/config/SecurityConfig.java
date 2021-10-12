package com.nowcoder.community.config;

import cn.hutool.crypto.digest.DigestUtil;
import com.nowcoder.community.constant.AuthConstant;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.service.LogoutService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.impl.UserServiceImpl;
import com.nowcoder.community.utils.CookieUtils;
import com.nowcoder.community.utils.JSONUtils;
import com.nowcoder.community.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/7:18
 * @Description:
 */
@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private CookieUtils cookieUtils;

    //不拦截静态资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    //用于登录，用户名和密码的校验
    //认证管理
    /** Authentication是认证管理的核心接口
     * AuthenticationManagerBuilder 用于创建AuthenticationManager
     * AuthenticationManager的实现类之一是 ProviderManager
     * ProviderManager 有一组 AuthenticationProvider，，每一个 AuthenticationProvider用于不同的认证管理
     * @param
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new AuthenticationProvider() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                String name = authentication.getName();//获取用户名
//                String password = (String) authentication.getCredentials();//获取密码
//                User user = userService.getUserByName(name);
//                if (user == null){
//                    throw new UsernameNotFoundException("用户名不存在");
//                }
//                String inputPwd = DigestUtil.md5Hex(password + user.getSalt());
//                if(!inputPwd.equals(password)){
//                    throw new UsernameNotFoundException("密码错误");
//                }
//
//                return new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities());
//            }
//            //判断是否支持这种认证方式
//            @Override
//            public boolean supports(Class<?> aClass) {
//                return UsernamePasswordAuthenticationToken.class.equals(aClass);
//            }
//        });
//    }
    //授权管理  目前只做授权不做认证，项目进行大半了

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //添加一个filter用于验证码的验证,在用户名密码验证之前
//        http.addFilterBefore(new Filter() {
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//                request = (HttpServletRequest)request;
//                String kaptchaCode = request.getParameter("kaptchaCode");
//                String uuid = cookieUtils.getCookieValue((HttpServletRequest) request, "uuid");
//                if(kaptchaCode == null || uuid == null){
//                    request.setAttribute("error","验证码错误");
//                    request.getRequestDispatcher("/toLogin").forward(request,response);
//                }
//                String kaptchaKey = RedisKeyUtils.getKaptchaKey(uuid);
//                String code = redisTemplate.opsForValue().get(kaptchaKey);
//                if (code == null ||!code.equalsIgnoreCase(kaptchaCode)){
//                    request.setAttribute("error","验证码不正确");
//                    request.getRequestDispatcher("/toLogin").forward(request,response);
//                }
//                filterChain.doFilter(request,response);
//            }
//        }, UsernamePasswordAuthenticationFilter.class).antMatcher("/login");

//        //自定义登录页面
//        http.formLogin().loginPage("/toLogin")
//                .loginProcessingUrl("/login")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        //登录成功重定向到首页
//                        response.sendRedirect(request.getContextPath()+"/");
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
//                        //登录失败转发到登录页面携带数据过去，所以用转发
//                        request.setAttribute("error",e.getMessage());
//                        request.getRequestDispatcher("/toLogin").forward(request,response);
//                    }
//                });
        //退出的相关设置
        http.logout()
                .logoutUrl("/NotLogout");
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                       //退出登录令牌失效
//                        String ticket = cookieUtils.getCookieValue(request, "ticket");
//                        logoutService.logout(ticket);
//                        //重定向到登录页面
//                        response.sendRedirect(request.getContextPath()+"/toLogin");
//                    }
//                });
        //授权配置
        http.authorizeRequests()
                .antMatchers(//三个类型用户都有的权限
                        "/comment/reply",
                        "/discussPost/**",
                        "/follow/**",
                        "/like",
                        "/message/**",
                        "/user/**"
                )
                .hasAnyAuthority(
                        AuthConstant.AUTH_USER,
                        AuthConstant.AUTH_ADMIN,
                        AuthConstant.AUTH_MODERATOR
                )
                .anyRequest()
                .permitAll().and().csrf().disable();//关闭csrf防护
        //记住我
        http.rememberMe()
                .tokenValiditySeconds(3600)//令牌有效时间
                .tokenRepository(new InMemoryTokenRepositoryImpl())//令牌存储的策略
                .userDetailsService(userService);//用户信息


        //权限不足时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //没有登录,两种情况，一种同步请求，一种异步请求
                        String header = request.getHeader("x-request-with");
                        if("XMLHttpRequest".equals(header)){
                            //异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(JSONUtils.getJSONString(403,"还没有登录"));
                        }else{
                            response.sendRedirect(request.getContextPath()+"/toLogin");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                //权限不足拒绝访问
                String header = request.getHeader("x-request-with");
                if("XMLHttpRequest".equals(header)){
                    //异步请求
                    response.setContentType("application/plain;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSONUtils.getJSONString(403,"权限不足，不能访问"));
                }else{
                    response.sendRedirect(request.getContextPath() + "/denied");
                }
            }
        });
    }
}