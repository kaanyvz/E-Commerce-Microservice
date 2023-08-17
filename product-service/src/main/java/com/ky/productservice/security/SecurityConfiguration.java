package com.ky.productservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{

        MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

        return http
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(builder.pattern(HttpMethod.GET,"v1/products/**")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/products")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/categories")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET,"v1/categories/**")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET,"v1/comments/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> csrf.disable())
                .build();

    }

    @Bean
    public AuditorAwareImpl auditorAware(){
        return new AuditorAwareImpl();
    }
}
