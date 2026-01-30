package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestPath = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // DEBUG LOG 1: Check if request is hitting the filter
        System.out.println("------------------------------------------------");
        System.out.println("🔍 [Filter] Processing Request: " + request.getMethod() + " " + requestPath);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [Filter] No Valid Authorization Header found (Anonymous request)");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUsername(jwt);
            Long userId = jwtService.extractUserId(jwt);
            String role = jwtService.extractRole(jwt);

            // DEBUG LOG 2: Check what is inside the token
            System.out.println("🔑 [Filter] Token Decoded:");
            System.out.println("   - User: " + userEmail);
            System.out.println("   - ID: " + userId);
            System.out.println("   - Raw Role in Token: " + role);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(jwt, userEmail)) {

                    // Create User object
                    User user = User.builder()
                            .id(userId)
                            .email(userEmail)
                            .role(role)
                            .build();

                    // DEBUG LOG 3: Check the final Authority being granted
                    System.out.println("🛡️ [Filter] Authorities Granted: " + user.getAuthorities());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ [Filter] Authentication set in SecurityContext");
                } else {
                    System.out.println("❌ [Filter] Token Validation Failed");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ [Filter] Error parsing token: " + e.getMessage());
        }

        System.out.println("------------------------------------------------");
        filterChain.doFilter(request, response);
    }
}