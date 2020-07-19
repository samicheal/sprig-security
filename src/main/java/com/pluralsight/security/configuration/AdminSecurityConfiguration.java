package com.pluralsight.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@Order(100)
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public void configure(HttpSecurity http) throws Exception{
        http.antMatcher("/support/admin/**")
                .addFilter(getDigestAuthFilter()).exceptionHandling()
                .authenticationEntryPoint(getDigestEntryPoint())
                .and()
                .authorizeRequests().antMatchers("/support/admin/**").hasRole("ADMIN");
    }

    public DigestAuthenticationFilter getDigestAuthFilter() throws Exception{
        DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
        filter.setUserDetailsService(userDetailsServiceBean());
        filter.setAuthenticationEntryPoint(getDigestEntryPoint());
        //for penetration test: lines 35 and 36
        filter.setPasswordAlreadyEncoded(true); // prevents filter from generating HA1 but uses one obtained from userdetails service
        filter.setCreateAuthenticatedToken(true); //forces digest to use the encrypted token from the userdetails service
        return filter;
    }

    private DigestAuthenticationEntryPoint getDigestEntryPoint(){
        // DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint(); //default from spring security

        // Custom DigestAuthenticationEntryPoint to override default stale=true property used for digest authentication
        CustomDigestAuthenticationEntryPointClass entryPoint = new CustomDigestAuthenticationEntryPointClass();
        entryPoint.setRealmName("admin-digest-realm");
        entryPoint.setKey("fjkf33DD312_+");
        return entryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        //.password("18d94adb9db016d4aed2502f88ca6e56") //password already encrypted using MD5
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("password1")
                .roles("USER")
        .and().withUser("admin")
                 .password("password2")
                .roles("ADMIN");
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
