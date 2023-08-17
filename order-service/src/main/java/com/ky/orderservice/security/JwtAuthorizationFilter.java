package com.ky.orderservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(!validString(authHeader) || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        logHeaders(request);
        String userId = request.getHeader("userId");
        List<Map<String, String>> authorities = new ArrayList<>();
        String authoritiesStr = request.getHeader("authorities");
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if(validString(authoritiesStr)){
            simpleGrantedAuthorities = Arrays.stream(authoritiesStr.split(",")).distinct()
                    .filter(this::validString).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, simpleGrantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }

    private void logHeaders(HttpServletRequest httpServletRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String header=headerNames.nextElement();
            logger.info(String.format("Header: %s --- Value: %s", header, httpServletRequest.getHeader(header)));

        }
    }

    private boolean validString(String input) {
        return input!=null && !input.isEmpty();
    }
}
