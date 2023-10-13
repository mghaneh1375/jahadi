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
//
//    protected void getUserWithOutCheckCompletenessVoid(HttpServletRequest request)
//            throws NotActivateAccountException, UnAuthException {
//
//        boolean auth = new JwtTokenFilter().isAuth(request);
//
//        Document u;
//        if (auth) {
//            u = userService.whoAmI(request);
//            if (u != null) {
//
//                if (!u.getString("status").equals("active")) {
//                    JwtTokenFilter.removeTokenFromCache(request.getHeader("Authorization").replace("Bearer ", ""));
//                    throw new NotActivateAccountException("Account not activated");
//                }
//
//                return;
//            }
//        }
//
//        throw new UnAuthException("Token is not valid");
//    }
//
//    protected Document getUserIfLogin(HttpServletRequest request) {
//
//        boolean auth = new JwtTokenFilter().isAuth(request);
//
//        Document u;
//        if (auth) {
//            u = userService.whoAmI(request);
//            if (u != null) {
//
//                if (!u.getString("status").equals("active"))
//                    return null;
//
//                return u;
//            }
//        }
//
//        return null;
//    }
//
//    private Document isWantedAccess(HttpServletRequest request, String wantedAccess
//    ) throws NotActivateAccountException, NotAccessException, UnAuthException {
//
//        if (JWT_TOKEN_FILTER.isAuth(request)) {
//
//            Document u = userService.whoAmI(request);
//
//            if (u != null) {
//
//                if (!u.getString("status").equals("active")) {
//                    JwtTokenFilter.removeTokenFromCache(request.getHeader("Authorization").replace("Bearer ", ""));
//                    throw new NotActivateAccountException("Account not activated");
//                }
//
//                if (wantedAccess.equals(Access.ADMIN.getName()) &&
//                        !Authorization.isAdmin(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//                if (wantedAccess.equals(Access.SCHOOL.getName()) &&
//                        !Authorization.isSchool(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//                if (wantedAccess.equals(Access.ADVISOR.getName()) &&
//                        !Authorization.isAdvisor(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//                if (wantedAccess.equals("quiz") &&
//                        !Authorization.isSchool(u.getList("accesses", String.class)) &&
//                        !Authorization.isAdvisor(u.getList("accesses", String.class))
//                )
//                    throw new NotAccessException("Access denied");
//
//                if (wantedAccess.equals(Access.TEACHER.getName()) &&
//                        !Authorization.isTeacher(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//
//                if (wantedAccess.equals(Access.AGENT.getName()) &&
//                        !Authorization.isAgent(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//
//
//                return u;
//            }
//        }
//
//        throw new UnAuthException("Token is not valid");
//    }
//
//    private Document isPrivilegeUser(HttpServletRequest request
//    ) throws NotActivateAccountException, NotAccessException, UnAuthException {
//
//        if (new JwtTokenFilter().isAuth(request)) {
//            Document u = userService.whoAmI(request);
//            if (u != null) {
//
//                if (!u.getString("status").equals("active")) {
//                    JwtTokenFilter.removeTokenFromCache(request.getHeader("Authorization").replace("Bearer ", ""));
//                    throw new NotActivateAccountException("Account not activated");
//                }
//
//                if (Authorization.isPureStudent(u.getList("accesses", String.class)))
//                    throw new NotAccessException("Access denied");
//
//                return u;
//            }
//        }
//
//        throw new UnAuthException("Token is not valid");
//    }
//
//    protected Document getUserWithAdminAccess(HttpServletRequest request,
//                                              boolean checkCompleteness,
//                                              boolean isPrivilege,
//                                              String userId
//    ) throws NotCompleteAccountException, UnAuthException, NotActivateAccountException, InvalidFieldsException {
//
//        Document user = checkCompleteness ? getUser(request) : getUserWithOutCheckCompleteness(request);
//
//        if (isPrivilege && Authorization.isPureStudent(user.getList("accesses", String.class)))
//            throw new InvalidFieldsException("Access denied");
//
//        boolean isAdmin = Authorization.isAdmin(user.getList("accesses", String.class));
//
//        if (userId != null && !isAdmin)
//            throw new InvalidFieldsException("no access");
//
//        if (userId != null && !ObjectId.isValid(userId))
//            throw new InvalidFieldsException("invalid objectId");
//
//        if (userId != null)
//            user = userRepository.findById(new ObjectId(userId));
//
//        if (user == null)
//            throw new InvalidFieldsException("invalid userId");
//
//        return new Document("user", user).append("isAdmin", isAdmin);
//    }
//
//    protected Document getUserWithSchoolAccess(HttpServletRequest request,
//                                               boolean checkCompleteness,
//                                               boolean isPrivilege,
//                                               String userId
//    ) throws NotCompleteAccountException, UnAuthException, NotActivateAccountException, InvalidFieldsException {
//
//        Document user = checkCompleteness ? getUser(request) : getUserWithOutCheckCompleteness(request);
//
//        if (isPrivilege && Authorization.isPureStudent(user.getList("accesses", String.class)))
//            throw new InvalidFieldsException("Access denied");
//
//        boolean isAdmin = Authorization.isAdmin(user.getList("accesses", String.class));
//        boolean isSchool = Authorization.isSchool(user.getList("accesses", String.class));
//
//        if (userId != null && !isAdmin && !isSchool)
//            throw new InvalidFieldsException("no access");
//
//        if (userId != null && !ObjectId.isValid(userId))
//            throw new InvalidFieldsException("invalid objectId");
//
//
//        if (userId != null) {
//
//            ObjectId oId = new ObjectId(userId);
//            if(isSchool && !isAdmin &&
//                    !Authorization.hasAccessToThisStudent(oId, user.getObjectId("_id"))
//            )
//                throw new InvalidFieldsException("Access denied");
//
//            user = userRepository.findById(oId);
//        }
//
//        if (user == null)
//            throw new InvalidFieldsException("invalid userId");
//
//        return new Document("user", user).append("isAdmin", isSchool);
//    }
//
//
//    protected Document getUserWithAdvisorAccess(HttpServletRequest request,
//                                               boolean weakAccess,
//                                               String userId
//    ) throws UnAuthException, NotActivateAccountException, InvalidFieldsException {
//
//        Document user = getUserWithOutCheckCompleteness(request);
//
//        boolean isAdmin = Authorization.isAdmin(user.getList("accesses", String.class));
//        boolean isAdvisor = Authorization.isAdvisor(user.getList("accesses", String.class));
//
//
//        if (userId != null && !isAdmin && !isAdvisor)
//            throw new InvalidFieldsException("no access");
//
//        if (userId != null && !ObjectId.isValid(userId))
//            throw new InvalidFieldsException("invalid objectId");
//
//        if (userId != null) {
//
//            ObjectId oId = new ObjectId(userId);
//
//            if(isAdvisor && !isAdmin &&
//                    (weakAccess && !Authorization.hasWeakAccessToThisStudent(oId, user.getObjectId("_id"))) ||
//                    (!weakAccess && !Authorization.hasAccessToThisStudent(oId, user.getObjectId("_id")))
//            )
//                throw new InvalidFieldsException("Access denied");
//
//            user = userRepository.findById(oId);
//        }
//
//        if (user == null)
//            throw new InvalidFieldsException("invalid userId");
//
//        return new Document("user", user).append("isAdmin", isAdvisor);
//    }
}
