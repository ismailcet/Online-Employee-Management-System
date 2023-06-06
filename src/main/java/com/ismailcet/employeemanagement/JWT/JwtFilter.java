package com.ismailcet.employeemanagement.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final EmployeeDetailsService service;

    Claims claims = null;
    private String tc = null;

    public JwtFilter(JwtUtil jwtUtil, EmployeeDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches("/api/v1/employee/login")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                tc = jwtUtil.extractTc(token);
                claims = jwtUtil.extractAllClaims(token);
            }
            if(tc != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails employeeDetails = service.loadUserByUsername(tc);
                if(jwtUtil.validateToken(token, employeeDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(employeeDetails,null, employeeDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public boolean isSupervisor(){
        return "SUPERVISOR".equalsIgnoreCase((String) claims.get("Role"));
    }
    public boolean isManager(){
        return "MANAGER".equalsIgnoreCase((String) claims.get("Role"));
    }
    public boolean isEmployee(){
        return "EMPLOYEE".equalsIgnoreCase((String) claims.get("Role"));
    }

    public String getCurrentEmployee(){
        return tc;
    }
}
