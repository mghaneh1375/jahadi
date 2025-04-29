package four.group.jahadi.Service;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.DTO.SignUp.SignInData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.BadRequestException;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Utility.Cache;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.StaticValues.DEV_MODE;


@Service
public class UserService extends AbstractService<User> {

    private static final ArrayList<Cache> cachedToken = new ArrayList<>();
    public final static String PICS_FOLDER = "userPics";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<List<User>> list(Object... filters) {

        List<User> users = userRepository.findAll(
                (AccountStatus) filters[0], (Access) filters[1],
                filters[2] != null ? filters[2].toString() : null,
                filters[3] != null ? filters[3].toString() : null,
                filters[4] != null ? filters[4].toString() : null,
                (Sex) filters[5],
                filters[6] != null ? filters[6].toString() : null,
                filters[7] != null ? (ObjectId) filters[7] : null,
                filters[8] != null ? (Boolean) filters[8] : null
        );

        if (filters[8] != null && (Boolean) filters[8]) {
            users.forEach(user -> {

                AccountStatus accountStatus = AccountStatus.PENDING;

                if (user.getGroupId() != null) {
                    Optional<Group> tmp = groupRepository.findById(user.getGroupId());
                    if (tmp.isPresent())
                        accountStatus = tmp.get().isActive() ? AccountStatus.ACTIVE :
                                AccountStatus.PENDING;
                }

                user.setGroupStatus(accountStatus);

            });
        }

        return new ResponseEntity<>(
                users,
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<User> findById(ObjectId id, Object... params) {

        if (id == null)
            throw new BadRequestException();

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (params.length > 0) {
            if (!Objects.equals(params[0], user.getGroupId()))
                throw new NotAccessException();
        }

        if(user.getGroupId() != null) {
            groupRepository.findById(user.getGroupId())
                    .ifPresent(value -> {
                        user.setGroupCode(value.getCode());
                        user.getAccesses().stream().filter(access -> access.equals(Access.GROUP))
                                .findFirst()
                                .ifPresent(access -> user.setGroupPic(value.getPic()));
                    });
        }

        return new ResponseEntity<>(
                user,
                HttpStatus.OK
        );

    }

    public ResponseEntity<String> signIn(SignInData data) {
        try {
//            if (!DEV_MODE) {
//                for (int i = 0; i < cachedToken.size(); i++) {
//                    if (cachedToken.get(i).equals(data)) {
//                        if (cachedToken.get(i).checkExpiration())
//                            return new ResponseEntity<>(
//                                    (String) cachedToken.get(i).getValue(),
//                                    HttpStatus.OK
//                            );
//
//                        cachedToken.remove(i);
//                        break;
//                    }
//                }
//            }

            Optional<User> user = userRepository.findByNID(data.getNid());
            if (user.isEmpty() || user.get().getRemoveAt() != null)
                throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");

            User u = user.get();

            if (!DEV_MODE) {
                if (!passwordEncoder.matches(data.getPassword(), u.getPassword()))
                    throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");
            }

            if (!u.getStatus().equals(AccountStatus.ACTIVE))
                throw new InvalidFieldsException("اکانت شما غیرفعال می باشد.");

            String token = jwtTokenProvider.createToken(data.getNid(), u.getAccesses(), u.getGroupId(), u.getId());

//            if (!DEV_MODE)
//                cachedToken.add(new Cache(TOKEN_EXPIRATION, token, data));

            return new ResponseEntity<>(
                    token, HttpStatus.OK
            );

        } catch (AuthenticationException x) {
            throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");
        }
    }

    public ResponseEntity<String> groupSignIn(AdminSignInData data, ObjectId groupId) {

        try {
            User u = userRepository.findByNID(data.getNid()).orElseThrow(InvalidIdException::new);

            if (!u.getGroupId().equals(groupId))
                throw new NotAccessException();

            String token = jwtTokenProvider.createToken(data.getNid(), u.getAccesses(), u.getGroupId(), u.getId());

            return new ResponseEntity<>(
                    token, HttpStatus.OK
            );

        } catch (AuthenticationException x) {
            throw new InvalidFieldsException("نام کاربری اشتباه است.");
        }
    }

    public ResponseEntity<String> adminSignIn(AdminSignInData data) {

        try {

            User u = userRepository.findByNID(data.getNid()).orElseThrow(InvalidIdException::new);
            String token = jwtTokenProvider.createToken(data.getNid(), u.getAccesses(), u.getGroupId(), u.getId());

            return new ResponseEntity<>(
                    token, HttpStatus.OK
            );

        } catch (AuthenticationException x) {
            throw new InvalidFieldsException("نام کاربری اشتباه است.");
        }
    }

    public void logout(String token) {

//        for (int i = 0; i < cachedToken.size(); i++) {
//            if (cachedToken.get(i).getValue().equals(token)) {
//                cachedToken.remove(i);
//                return;
//            }
//        }

        JwtTokenProvider.removeTokenFromCache(token.replace("Bearer ", ""));

    }

    public User whoAmI(HttpServletRequest req) {
        try {

            Optional<User> u = userRepository.findByNID(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));

            if (u.isEmpty() || u.get().getRemoveAt() != null)
                return null;

            return u.get();
        } catch (Exception x) {
            return null;
        }
    }

    public void removeFromGroup(ObjectId userId, ObjectId groupId) {
        if (groupId == null)
            throw new NotAccessException();

        User u = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if (u.getGroupId() == null || !u.getGroupId().equals(groupId))
            throw new NotAccessException();

        u.setGroupName(null);
        u.setGroupId(null);
        userRepository.save(u);
    }

    public ResponseEntity<List<User>> findGroupMembersByRegionOwner(ObjectId userId, ObjectId groupId) {

        List<Trip> trips =
                tripRepository.findActivesOrNotStartedProjectIdsByAreaOwnerId(Utility.getCurrLocalDateTime(), userId);

        if (trips.size() == 0)
            throw new NotAccessException();

        return new ResponseEntity<>(
                userRepository.findAll(
                                AccountStatus.ACTIVE, Access.JAHADI,
                                null, null, null, null,
                                null, groupId, null
                        ).stream()
                        .filter(user -> !user.getId().equals(userId) && !user.getAccesses().contains(Access.GROUP))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    public void remove(ObjectId userId) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setRemoveAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public ResponseEntity<User> info(ObjectId userId) {

        User user = userRepository.findDigestById(userId).orElseThrow(InvalidIdException::new);

        user.setRole(user.getAccesses().contains(Access.ADMIN) ? Access.ADMIN :
                user.getAccesses().contains(Access.GROUP) ? Access.GROUP : Access.JAHADI
        );

        if (Objects.equals(user.getRole(), Access.JAHADI)) {
            LocalDateTime currDate = Utility.getCurrLocalDateTime();
            user.setHasActiveRegion(tripRepository.existNotFinishedByAreaOwnerId(currDate, userId));
            user.setHasActiveTask(tripRepository.existNotFinishedByResponsibleId(currDate, userId));
        }

        return new ResponseEntity<>(
                user,
                HttpStatus.OK
        );
    }
}
