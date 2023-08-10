import UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtUtil {

    private final String secretKey = "3124326756876909768784632412E43623351463524627247321351873751481378213758241538124385521857358713758438924861284432E243E54234E2334766890";
    private final long jwtExpiration = 8 * 60 * 60 * 1000;// 8 saatlik token

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDto user) {
        return buildToken(new HashMap<>(), user);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDto user
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("roles", user.getRoles().stream().map(role -> role.getName()).collect(Collectors.joining(",")))
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public List<GrantedAuthority> getAuthorities(String token) {
        String roles = (String) extractAllClaims(token).get("roles");
        return Arrays.stream(roles.split(","))
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());
    }
}