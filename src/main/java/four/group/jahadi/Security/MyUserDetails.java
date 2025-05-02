package four.group.jahadi.Security;

import four.group.jahadi.Enums.Access;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MyUserDetails implements UserDetailsService {

    @Value("${token.secret.key.custom}")
    private String secretKey;

    private static String sharedKeyBytes = null;

    public String getSharedKeyBytes() {
        if(sharedKeyBytes == null)
            sharedKeyBytes = Base64.getEncoder().encodeToString(secretKey.getBytes());

        return sharedKeyBytes;
    }

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        try {
            Claims claims = Jwts.parser().setSigningKey(getSharedKeyBytes()).parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .authorities((Collection<? extends GrantedAuthority>) claims.get("roles", List.class).stream().map(o -> Access.valueOf(((HashMap<?, ?>) o).get("authority").toString())).collect(Collectors.toList()))
                    .password("")
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        }
        catch (Exception x) {
            throw new UsernameNotFoundException("token is not valid");
        }
    }

}