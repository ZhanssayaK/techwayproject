package kz.project.techway.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.project.techway.exceptions.TokenNotFoundException;
import kz.project.techway.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();

        if (isAuthenticationRequest(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(request.getHeader("Authorization"));

        try {
            if (Objects.nonNull(jwt) && tokenService.isTokenExpired(jwt)) {
                throw new TokenNotFoundException("Token has expired");
            }

            String username = tokenService.extractUsername(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (Boolean.FALSE.equals(tokenService.exists(username))) {
                throw new TokenNotFoundException("Token does not exist for the user, login again");
            }

            if (Boolean.TRUE.equals(tokenService.isTokenNeedsRefresh(jwt))) {
                String newToken = tokenService.generateRefreshToken(jwt);
                tokenService.saveToken(username, newToken);
                response.setHeader("Authorization", "Bearer " + newToken);
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            return;
        } catch (TokenNotFoundException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticationRequest(String requestPath) {
        return requestPath.contains("/api/auth");
    }

    private String extractJwtFromHeader(String authHeader) {
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
