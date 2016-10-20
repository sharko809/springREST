package com.serviceapp.config;

import com.serviceapp.filter.AuthenticationTokenProcessingFilter;
import com.serviceapp.filter.LoginFilter;
import com.serviceapp.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Configuration for Spring Security
 */
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

//    @Bean
//    public AbstractAccessDecisionManager accessDecisionManager() {
//        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
//        decisionVoters.add(new RoleVoter());
//        return new UnanimousBased(decisionVoters);
//    }
//
//    @Bean
//    public FilterSecurityInterceptor filterSecurityInterceptor() {
//        FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
//        interceptor.setAccessDecisionManager(accessDecisionManager());
//        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
//        List<ConfigAttribute> adminConfig = new ArrayList<>(Collections.singletonList(new SecurityConfig("ROLE_ADMIN")));
//        List<ConfigAttribute> accountConfig = new ArrayList<>(Collections.singletonList(new SecurityConfig("ROLE_USER")));
//        List<ConfigAttribute> postReviewConfig = new ArrayList<>(
//                Arrays.asList(new SecurityConfig("ROLE_USER"), new SecurityConfig("ROLE_ADMIN")));
//
//        requestMap.put(new AntPathRequestMatcher("/admin/**"), adminConfig);
//        requestMap.put(new AntPathRequestMatcher("/account/**"), accountConfig);
//        requestMap.put(new AntPathRequestMatcher("/movies/*/post", HttpMethod.POST.toString()), postReviewConfig);
//        interceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(requestMap));
//        return interceptor;
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public org.springframework.security.web.access.AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public GenericFilterBean loginFilter() {
        return new LoginFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthFailureHandler();
    }

    @Bean(name = "authManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
        return new AuthenticationTokenProcessingFilter("/admin/**", authenticationManagerBean(), authenticationSuccessHandler());
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilterAccount() throws Exception {
        return new AuthenticationTokenProcessingFilter("/account/**", authenticationManagerBean(), authenticationSuccessHandler());
    }

    @Bean
    public AbstractAuthenticationProcessingFilter authenticationProcessingFilterPostReview() throws Exception {
        return new AuthenticationTokenProcessingFilter("/movies/*/post", authenticationManagerBean(), authenticationSuccessHandler());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/movies/*").authenticated()
                .antMatchers("/movies", "/top", "/search**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/account", "/account/**").authenticated()
                .mvcMatchers(HttpMethod.POST, "/").anonymous()
//                .mvcMatchers(HttpMethod.GET, "/").anonymous()
                .mvcMatchers(HttpMethod.POST, "/login").anonymous()
                .mvcMatchers(HttpMethod.GET, "/login").anonymous()
                .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
            .addFilterAfter(authenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(authenticationProcessingFilterAccount(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(authenticationProcessingFilterPostReview(), UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .usernameParameter("login")
                .passwordParameter("password")
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
            .csrf().disable();
    }

}
