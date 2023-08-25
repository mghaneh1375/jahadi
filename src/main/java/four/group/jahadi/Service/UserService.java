package four.group.jahadi.Service;

import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.FilteringFactory;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static four.group.jahadi.Utility.Utility.generateErr;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;


@Service
public class UserService extends AbstractService<User, UserData> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getEncPass(String pass) {
        return passwordEncoder.encode(Utility.convertPersianDigits(pass));
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public String list(Object... filters) {
        return generateSuccessMsg("data", convertObjectsToJSONList(userRepository.findAll()));

    }

    @Override
    String update(ObjectId id, UserData dto) {
        return null;
    }

    @Override
    public String store(UserData dto) {

        if(userRepository.countByPhone(dto.getPhone()) > 0)
            return generateErr("شماره همراه وارد شده در سیستم موجود است");

        if(userRepository.countByNID(dto.getNid()) > 0)
            return generateErr("کد ملی وارد شده در سیستم موجود است");


        dto.setPassword(getEncPass(dto.getPassword()));
//        PairValue existTokenP = existSMS(jsonObject.getString("username"));
//
//        if (existTokenP != null)
//            return generateSuccessMsg("token", existTokenP.getKey(),
//                    new PairValue("reminder", existTokenP.getValue())
//            );

        User user = userRepository.insert(populateEntity(null, dto));
        // todo : send activation code
        return generateSuccessMsg("id", user.get_id());
    }

//    public static PairValue existSMS(String username) {
//
//        Document doc = activationRepository.findOne(
//                and(
//                        eq("username", username),
//                        gt("created_at", System.currentTimeMillis() - SMS_RESEND_MSEC)
//                )
//                , new BasicDBObject("token", 1).append("created_at", 1)
//        );
//
//        if (doc != null)
//            return new PairValue(doc.getString("token"), SMS_RESEND_SEC - (System.currentTimeMillis() - doc.getLong("created_at")) / 1000);
//
//        return null;
//    }

    @Override
    User populateEntity(User user, UserData userData) {

        boolean isNew = false;

        if(user == null) {
            user = new User();
            isNew = true;
        }

        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setPhone(userData.getPhone());
        user.setNID(userData.getNid());
        user.setEducationalField(userData.getEducationalField());
        user.setBirthDay(userData.getBirthDay());
        user.setSpecification(userData.getSpecification());
        user.setSex(userData.getSex().equals(Sex.MALE.getName()) ? Sex.MALE : Sex.FEMALE);

        if(isNew) {
            user.setAccesses(new ArrayList<>() {{
                add(Access.JAHADI);
            }});
            user.setStatus(AccountStatus.PENDING);
        }

        return user;
    }

    @Override
    User findById(ObjectId id) {
        return null;
    }

    public User findByPhone(String phone) {

        Pageable pageable = PageRequest.of(0, 1);

        List<String> filters = new ArrayList<>();
        filters.add("phone|eq|" + phone);

        List<User> tmp = userRepository.findAllWithFilter(User.class,
                FilteringFactory.parseFromParams(filters, User.class), pageable
        ).toList();

        return tmp.size() > 0 ? tmp.get(0) : null;
    }
//
//    public String toggleStatus(ObjectId userId) {
//
//        Document user = userRepository.findById(userId);
//        if(user == null)
//            return null;
//
//        switch (user.getString("status")) {
//            case "active":
//                user.put("status", "deActive");
//                break;
//            case "deActive":
//                user.put("status", "active");
//                break;
//            default:
//                return null;
//        }
//
//        userRepository.updateOne(
//                eq("_id", userId),
//                set("status", user.getString("status"))
//        );
//
//        return Utility.generateSuccessMsg("newStatus", user.getString("status"));
//    }
//
//    public String signIn(String username, String password, boolean checkPass
//    ) throws NotActivateAccountException {
//
//        try {
//
////            if(checkPass) {
////                PairValue p = new PairValue(username, password);
//
////                for (int i = 0; i < cachedToken.size(); i++) {
////                    if (cachedToken.get(i).equals(p)) {
////                        if (cachedToken.get(i).checkExpiration())
////                            return (String) cachedToken.get(i).getValue();
////
////                        cachedToken.remove(i);
////                        break;
////                    }
////                }
////            }
//
//            Document user = userRepository.findByUnique(username, false);
//
//            if(user == null || user.containsKey("remove_at"))
//                throw new CustomException("نام کاربری و یا رمزعبور اشتباه است.", HttpStatus.UNPROCESSABLE_ENTITY);
//
//            if (!DEV_MODE && checkPass) {
//                if (!passwordEncoder.matches(password, user.getString("password")))
//                    throw new CustomException("نام کاربری و یا رمزعبور اشتباه است.", HttpStatus.UNPROCESSABLE_ENTITY);
//            }
//
//            if (!user.getString("status").equals("active"))
//                throw new NotActivateAccountException("اکانت شما غیرفعال شده است.");
//
//            username = user.containsKey("phone") ?
//                    user.getString("phone") :
//                    user.getString("mail");
//
//            String token = jwtTokenProvider.createToken(username, (user.getBoolean("level")) ? Role.ROLE_ADMIN : Role.ROLE_CLIENT);
//
////            if(checkPass)
////                cachedToken.add(new Cache(TOKEN_EXPIRATION, token, new PairValue(user.getString("username"), password)));
//
//            return Utility.generateSuccessMsg(
//                    "token", token,
//                    new PairValue("user", UserController.isAuth(user))
//            );
//
//        } catch (AuthenticationException x) {
//            throw new CustomException("نام کاربری و یا رمزعبور اشتباه است.", HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//    }
//
//    public void logout(String token) {
//        for (int i = 0; i < cachedToken.size(); i++) {
//            if (cachedToken.get(i).getValue().equals(token)) {
//                cachedToken.remove(i);
//                return;
//            }
//        }
//    }
//
//    public Document whoAmI(HttpServletRequest req) {
//        try {
//
//            Document u = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req), false));
//
//            if(u == null || u.containsKey("remove_at"))
//                return null;
//
//            return u;
//        }
//        catch (Exception x) {
//            return null;
//        }
//    }

}
