package four.group.jahadi.Security;

import four.group.jahadi.Exception.CustomException;
import four.group.jahadi.Utility.PairValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static four.group.jahadi.Utility.StaticValues.TOKEN_EXPIRATION;


// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    public class ValidateToken {

        String token;
        long issue;

        ValidateToken(String token) {
            this.token = token;
            this.issue = System.currentTimeMillis();
        }

        public boolean isValidateYet() {
            return System.currentTimeMillis() - issue <= TOKEN_EXPIRATION; // 1 week
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof String)
                return o.equals(token);
            return false;
        }
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
//    public static final ArrayList<ValidateToken> validateTokens = new ArrayList<>();
    public static final ArrayList<PairValue> blackListTokens = new ArrayList<>();

    public static void removeTokenFromCache(String token) {

//        for(int i = 0; i < validateTokens.size(); i++) {
//            if(validateTokens.get(i).token.equals(token)) {
//                blackListTokens.add(new PairValue(token, TOKEN_EXPIRATION + validateTokens.get(i).issue));
//                validateTokens.remove(i);
//                return;
//            }
//        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        isAuth(httpServletRequest);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public boolean isAuth(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);

        if(token != null) {

//            for (PairValue blackListToken : blackListTokens) {
//                if (blackListToken.getKey().equals(token))
//                    return false;
//            }
//
//            for(Iterator<ValidateToken> iterator = validateTokens.iterator(); iterator.hasNext();) {
//
//                ValidateToken v = iterator.next();
//                if(v == null) {
//                    iterator.remove();
//                    continue;
//                }
//
//                if(v.equals(token)) {
//
//                    if(v.isValidateYet())
//                        return true;
//                    else
//                        iterator.remove();
//
//                    break;
//                }
//            }
        }

        try {
            if (token != null && jwtTokenProvider.validateAuthToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
//                validateTokens.add(new ValidateToken(token));
                return true;
            }
        } catch (CustomException ex) {
            SecurityContextHolder.clearContext();
            return false;
        }

        return false;
    }
}
