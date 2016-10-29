package com.serviceapp.config;

import com.serviceapp.filter.AuthFilter;
import com.serviceapp.filter.JwtAuthFilter;
import com.serviceapp.security.AccessDeniedHandler;
import com.serviceapp.security.RestAuthenticationEntryPoint;
import com.serviceapp.security.UserDetailsServiceImpl;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

/**
 * Configuration for Spring Security
 */
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public Filter authFilter() throws Exception {
        return new AuthFilter(authenticationManager());
    }

    @Bean
    public Filter adminTokenFilter() {
        return new JwtAuthFilter(new AntPathRequestMatcher("/admin/**"), authenticationManager);
    }

    @Bean
    public Filter accountTokenFilter() {
        return new JwtAuthFilter(new AntPathRequestMatcher("/account/**"), authenticationManager);
    }

    @Bean
    public Filter reviewTokenFilter() {
        return new JwtAuthFilter(new AntPathRequestMatcher("/movies/**/post**"), authenticationManager);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin**", "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/movies**", "/movies/**", "/search**").permitAll()
                .antMatchers(HttpMethod.POST, "/registration").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .httpBasic().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .addFilterBefore(adminTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accountTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(reviewTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable();
    }

}
