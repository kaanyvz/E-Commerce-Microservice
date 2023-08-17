package com.ky.userservice.config;

import com.ky.userservice.filter.JwtAccessDeniedHandler;
import com.ky.userservice.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfiguration(AuthenticationConfiguration authenticationConfiguration, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           HandlerMappingIntrospector introspector)throws Exception{


        MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/user/register")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/user/login")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/token/refresh")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/user/validateToken")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/me")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/getById/{userId}")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/user/add")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.PUT, "v1/user/update")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.PUT, "v1/user/updatePassword")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/find/{email}")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/resetpassword/{email}")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.DELETE, "v1/user/delete/{email}")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.POST, "v1/user/updateProfileImage")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/image/{email}/{fileName}")).permitAll()
                        .requestMatchers(builder.pattern(HttpMethod.GET, "v1/user/image/profile/{email}")).permitAll()

                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.accessDeniedHandler(jwtAccessDeniedHandler))
                .headers(headers -> headers.frameOptions().disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();


    }

}
