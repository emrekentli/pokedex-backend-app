import TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService service;
    private final TokenRepository repository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String autheader = request.getHeader("Authorization");
        final String token;
        final String username;

        if (autheader == null || !autheader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = autheader.substring(7);
        username = jwtUtil.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = service.loadUserByUsername(username);
            String tokenFromRepository = repository.getUserTokenByUserName(username);
            List<GrantedAuthority> grantedAuthorities = jwtUtil.getAuthorities(token);


            if (jwtUtil.isTokenValid(token, userDetails) && tokenFromRepository!= null && tokenFromRepository.equals(token)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        grantedAuthorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
