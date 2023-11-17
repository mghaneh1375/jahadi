package four.group.jahadi.Routes;

import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Models.User;
import four.group.jahadi.Security.JwtTokenFilter;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class Router {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    protected ObjectId getGroup(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            String token = bearerToken.substring(7);

            Claims claims;
            try {

                claims = Jwts.parser()
                        .setSigningKey(jwtTokenProvider.getSharedKeyBytes())
                        .parseClaimsJws(token)
                        .getBody();

                return new ObjectId(claims.get("groupId").toString());

            } catch (Exception ignore) {}
        }

        return null;
    }

    protected ObjectId getId(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

            String token = bearerToken.substring(7);

            Claims claims;
            try {

                claims = Jwts.parser()
                        .setSigningKey(jwtTokenProvider.getSharedKeyBytes())
                        .parseClaimsJws(token)
                        .getBody();

                return new ObjectId(claims.get("id").toString());

            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

        return null;
    }

    protected User getUser(HttpServletRequest request)
            throws NotActivateAccountException, UnAuthException {

        boolean auth = jwtTokenFilter.isAuth(request);

        if (auth) {

            User u = userService.whoAmI(request);

            if (u != null) {
                if (!u.getStatus().equals(AccountStatus.ACTIVE)) {
                    JwtTokenFilter.removeTokenFromCache(request.getHeader("Authorization").replace("Bearer ", ""));
                    throw new NotActivateAccountException("Account not activated");
                }

                return u;
            }
        }

        throw new UnAuthException("Token is not valid");
    }

    protected User getUserWithOutCheckCompleteness(HttpServletRequest request)
            throws NotActivateAccountException, UnAuthException {

        boolean auth = jwtTokenFilter.isAuth(request);

        if (auth) {
            User u = userService.whoAmI(request);

            if (u != null) {
                if (u.getStatus().equals(AccountStatus.BLOCKED)) {
                    JwtTokenFilter.removeTokenFromCache(request.getHeader("Authorization").replace("Bearer ", ""));
                    throw new NotActivateAccountException("اکانت شما غیر فعال است");
                }

                return u;
            }
        }

        throw new UnAuthException("Token is not valid");
    }

}
