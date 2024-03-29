package four.group.jahadi.Security;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.CustomException;
import four.group.jahadi.Utility.PairValue;
import io.jsonwebtoken.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Security.JwtTokenFilter.blackListTokens;
import static four.group.jahadi.Utility.StaticValues.TOKEN_EXPIRATION;
import static four.group.jahadi.Utility.StaticValues.TOKEN_EXPIRATION_MSEC;


@Component
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    final static private String secretKey = "{MIP0kK^PGU;l/{";

    @Autowired
    private MyUserDetails myUserDetails;

    public String getSharedKeyBytes() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<Access> roles, ObjectId groupId, ObjectId id) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles.stream().map(e -> new SimpleGrantedAuthority(e.getAuthority())).collect(Collectors.toList()));
        claims.put("groupId", groupId == null ? null : groupId.toString());
        claims.put("id", id.toString());

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_EXPIRATION_MSEC);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, getSharedKeyBytes())
                .compact();
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(getSharedKeyBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    boolean validateAuthToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSharedKeyBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void removeTokenFromCache(String token) {
        blackListTokens.add(new PairValue(token, TOKEN_EXPIRATION + System.currentTimeMillis()));
    }

}
