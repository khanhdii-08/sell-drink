package com.example.ProductService.config;

import com.example.ProductService.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/v1/api/watters").hasAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/v1/api/watters/ids").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(HttpMethod.DELETE, "/v1/api/watters/**").hasAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/v1/api/watters/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
