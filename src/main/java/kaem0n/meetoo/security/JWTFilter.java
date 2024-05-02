package kaem0n.meetoo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools tools;
    @Autowired
    private UserService us;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnauthorizedException("Invalid authorization header.");
        else {
            String token = authHeader.replace("Bearer ", "");
            tools.verifyToken(token);
            String userId = tools.getIdFromToken(token);
            User current = us.findById(UUID.fromString(userId));
            Authentication a = new UsernamePasswordAuthenticationToken(current, null, current.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(a);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/api/auth/**", request.getServletPath());
    }
}
