package com.example.demo.filter;


import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Service
public class JWTAuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userInfoService;

    public JWTAuthFilter(TokenService tokenService, UserService userInfoService) {
        this.tokenService = tokenService;
        this.userInfoService = userInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println(request.toString());
        final String jwt;
        final String userIdentifier;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(7);
        userIdentifier = tokenService.extractUsername(jwt);
        if(userIdentifier != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userInfoService.loadUserByUsername(userIdentifier);
            if(userDetails != null){
                if(tokenService.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,null, Collections.singletonList(new SimpleGrantedAuthority("User"))
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);
        }
    }

}