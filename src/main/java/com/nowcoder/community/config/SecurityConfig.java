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
 * @Author: ?????????
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

    //?????????????????????
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    //??????????????????????????????????????????
    //????????????
    /** Authentication??????????????????????????????
     * AuthenticationManagerBuilder ????????????AuthenticationManager
     * AuthenticationManager????????????????????? ProviderManager
     * ProviderManager ????????? AuthenticationProvider??????????????? AuthenticationProvider???????????????????????????
     * @param
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new AuthenticationProvider() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                String name = authentication.getName();//???????????????
//                String password = (String) authentication.getCredentials();//????????????
//                User user = userService.getUserByName(name);
//                if (user == null){
//                    throw new UsernameNotFoundException("??????????????????");
//                }
//                String inputPwd = DigestUtil.md5Hex(password + user.getSalt());
//                if(!inputPwd.equals(password)){
//                    throw new UsernameNotFoundException("????????????");
//                }
//
//                return new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities());
//            }
//            //????????????????????????????????????
//            @Override
//            public boolean supports(Class<?> aClass) {
//                return UsernamePasswordAuthenticationToken.class.equals(aClass);
//            }
//        });
//    }
    //????????????  ??????????????????????????????????????????????????????

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //????????????filter????????????????????????,??????????????????????????????
//        http.addFilterBefore(new Filter() {
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//                request = (HttpServletRequest)request;
//                String kaptchaCode = request.getParameter("kaptchaCode");
//                String uuid = cookieUtils.getCookieValue((HttpServletRequest) request, "uuid");
//                if(kaptchaCode == null || uuid == null){
//                    request.setAttribute("error","???????????????");
//                    request.getRequestDispatcher("/toLogin").forward(request,response);
//                }
//                String kaptchaKey = RedisKeyUtils.getKaptchaKey(uuid);
//                String code = redisTemplate.opsForValue().get(kaptchaKey);
//                if (code == null ||!code.equalsIgnoreCase(kaptchaCode)){
//                    request.setAttribute("error","??????????????????");
//                    request.getRequestDispatcher("/toLogin").forward(request,response);
//                }
//                filterChain.doFilter(request,response);
//            }
//        }, UsernamePasswordAuthenticationFilter.class).antMatcher("/login");

//        //?????????????????????
//        http.formLogin().loginPage("/toLogin")
//                .loginProcessingUrl("/login")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        //??????????????????????????????
//                        response.sendRedirect(request.getContextPath()+"/");
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
//                        //?????????????????????????????????????????????????????????????????????
//                        request.setAttribute("error",e.getMessage());
//                        request.getRequestDispatcher("/toLogin").forward(request,response);
//                    }
//                });
        //?????????????????????
        http.logout()
                .logoutUrl("/NotLogout");
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                       //????????????????????????
//                        String ticket = cookieUtils.getCookieValue(request, "ticket");
//                        logoutService.logout(ticket);
//                        //????????????????????????
//                        response.sendRedirect(request.getContextPath()+"/toLogin");
//                    }
//                });
        //????????????
        http.authorizeRequests()
                .antMatchers(//?????????????????????????????????
                        "/comment/reply",
                        "/follow/**",
                        "/like",
                        "/message/**",
                        "/user/setting",
                        "/user/upload",
                        "/user/modifyPassword",
                        "/user/profile"
                )
                .hasAnyAuthority(
                        AuthConstant.AUTH_USER,
                        AuthConstant.AUTH_ADMIN,
                        AuthConstant.AUTH_MODERATOR
                ).antMatchers(
                        "/data/**"
                )
                .hasAnyAuthority(
                        AuthConstant.AUTH_ADMIN
                )
                .anyRequest()
                .permitAll().and().csrf().disable();//??????csrf??????
//        //?????????
//        http.rememberMe()
//                .tokenValiditySeconds(3600)//??????????????????
//                .tokenRepository(new InMemoryTokenRepositoryImpl())//?????????????????????
//                .userDetailsService(userService);//????????????


        //????????????????????????
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //????????????,??????????????????????????????????????????????????????
                        String header = request.getHeader("x-request-with");
                        if("XMLHttpRequest".equals(header)){
                            //????????????
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(JSONUtils.getJSONString(403,"???????????????"));
                        }else{
                            response.sendRedirect(request.getContextPath()+"/toLogin");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                //????????????????????????
                String header = request.getHeader("x-request-with");
                if("XMLHttpRequest".equals(header)){
                    //????????????
                    response.setContentType("application/plain;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSONUtils.getJSONString(403,"???????????????????????????"));
                }else{
                    response.sendRedirect(request.getContextPath() + "/denied");
                }
            }
        });
    }
}