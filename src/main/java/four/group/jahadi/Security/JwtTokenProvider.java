package four.group.jahadi.Security;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Utility.PairValue;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Security.JwtTokenFilter.blackListTokens;
import static four.group.jahadi.Utility.StaticValues.TOKEN_EXPIRATION;
import static four.group.jahadi.Utility.StaticValues.TOKEN_EXPIRATION_MSEC;


@Component
public class JwtTokenProvider {

    @Autowired
    private MyUserDetails myUserDetails;

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
                .signWith(SignatureAlgorithm.HS256, myUserDetails.getSharedKeyBytes())
                .compact();
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(myUserDetails.getSharedKeyBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static void removeTokenFromCache(String token) {
        blackListTokens.add(new PairValue(token, TOKEN_EXPIRATION + System.currentTimeMillis()));
    }

}
