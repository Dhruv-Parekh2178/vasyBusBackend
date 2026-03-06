package com.app.vasyBus.config.security;

import com.app.vasyBus.exception.ResourceNotFoundException;
import com.app.vasyBus.model.User;
import com.app.vasyBus.repository.UserRepository;
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
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        String userName = null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userName = jwtUtil.getUserNameFromToken(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":false,\"message\":\"fail\",\"data\":{\"message\":\"Invalid or Expired Token\",\"status\":401}}");
            return;
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(userName).orElseThrow(() ->new ResourceNotFoundException("User Not Found"));

            if (user != null && jwtUtil.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":false,\"message\":\"fail\",\"data\":{\"message\":\"Invalid or Expired Token\",\"status\":401}}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}