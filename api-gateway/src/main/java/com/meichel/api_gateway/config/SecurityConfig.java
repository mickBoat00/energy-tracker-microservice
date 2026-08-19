package com.meichel.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.meichel.api_gateway.filter.UserIdentityHeaderFilter;

@Configuration
public class SecurityConfig {

    @Value("${security.excluded.urls}")
    private String[] excludedUrls;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(excludedUrls).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public FilterRegistrationBean<UserIdentityHeaderFilter> userIdentityHeaderFilter() {
        FilterRegistrationBean<UserIdentityHeaderFilter> registration = new FilterRegistrationBean<>(
                new UserIdentityHeaderFilter());
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

}
