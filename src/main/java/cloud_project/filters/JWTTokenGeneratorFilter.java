package cloud_project.filters;

import cloud_project.constants.ApplicationConstants;
import cloud_project.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;

    public JWTTokenGeneratorFilter() {
        this.secretKey = Keys.hmacShaKeyFor(ApplicationConstants.JWT_SECRET_DEFAULT_VALUE.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("POST") && request.getServletPath().equals("/login")) {
            Authentication authentication = (Authentication) request.getAttribute("authentication");

            if (authentication != null && authentication.isAuthenticated()) {
                User user = (User) authentication.getPrincipal();
                String jwt = Jwts.builder()
                        .setIssuer("CloudProject")
                        .setSubject(user.getEmail())
                        .claim("authorities", user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(",")))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                        .signWith(secretKey, SignatureAlgorithm.HS256)
                        .compact();

                response.setHeader(ApplicationConstants.JWT_HEADER, "Bearer " + jwt);
            }
        }
        filterChain.doFilter(request, response);
    }
}
