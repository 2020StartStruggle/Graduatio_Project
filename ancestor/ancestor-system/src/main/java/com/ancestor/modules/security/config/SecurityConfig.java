package com.ancestor.modules.security.config;

import com.ancestor.annotation.AnonymousAccess;
import com.ancestor.config.ElPermissionConfig;
import com.ancestor.modules.security.security.JwtAuthenticationEntryPoint;
import com.ancestor.modules.security.security.JwtAuthorizationTokenFilter;
import com.ancestor.modules.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final ApplicationContext applicationContext;

    // ???????????????JWT??????????????????
    private final JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Value("${jwt.header}")
    private String tokenHeader;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, JwtUserDetailsService jwtUserDetailsService, JwtAuthorizationTokenFilter authenticationTokenFilter, ApplicationContext applicationContext) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // ?????? ???????????? url??? PreAuthorize("hasAnyRole('anonymous')") ??? PreAuthorize("@el.check('anonymous')") ??? AnonymousAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            if (null != preAuthorize && preAuthorize.value().toLowerCase().contains("anonymous")) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            } else if (null != anonymousAccess && null == preAuthorize) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        httpSecurity
                // ?????? CSRF
                .csrf().disable()
                // ????????????
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // ???????????????
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // ????????????
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).anonymous()
                // swagger start
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // swagger end
                // ??????
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                // ??????OPTIONS??????
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                // ???????????????????????????url?????? ??? ?????? ??????????????????????????????????????????
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                // ???????????????????????????
                .anyRequest().authenticated()
                // ??????iframe ????????????
                .and().headers().frameOptions().disable();
        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
