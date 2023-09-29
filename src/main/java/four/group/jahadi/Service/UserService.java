package four.group.jahadi.Service;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.BadRequestException;
import four.group.jahadi.Exception.InvalidCodeException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Activation;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ActivationRepository;
import four.group.jahadi.Repository.FilteringFactory;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Utility.Cache;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static four.group.jahadi.Utility.StaticValues.*;
import static four.group.jahadi.Utility.Utility.*;


@Service
public class UserService extends AbstractService<User, SignUpData> {

    private static ArrayList<Cache> cachedToken = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivationRepository activationRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String getEncPass(String pass) {
        return passwordEncoder.encode(convertPersianDigits(pass));
    }

    @Override
    public ResponseEntity<List<User>> list(Object... filters) {
        return new ResponseEntity<>(
                userRepository.findAll(
                        (AccountStatus) filters[0], (Access) filters[1],
                        filters[2] != null ? filters[2].toString() : null,
                        filters[3] != null ? filters[3].toString() : null,
                        filters[4] != null ? filters[4].toString() : null,
                        (Sex) filters[5],
                        filters[6] != null ? filters[6].toString() : null
                ),
                HttpStatus.OK
        );
    }

    @Override
    String update(ObjectId id, SignUpData dto, Object ... params) {
        return null;
    }

    public String checkUniqueness(SignUpStep1Data dto) {

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            return generateErr("شماره همراه وارد شده در سیستم موجود است");

        if (userRepository.countByNID(dto.getNid()) > 0)
            return generateErr("کد ملی وارد شده در سیستم موجود است");

        return JSON_OK;
    }

    public String checkGroup(SignUpStep3Data dto) {

        if(groupRepository.countByCode(dto.getGroupCode()) == 0)
            return generateErr("کد گروه نامعتبر است");

        return JSON_OK;
    }

    @Override
    public String store(SignUpData dto, Object ... params) {

        if(userRepository.countByPhone(dto.getPhone()) > 0)
            return generateErr("شماره همراه وارد شده در سیستم موجود است");

        if(userRepository.countByNID(dto.getNid()) > 0)
            return generateErr("کد ملی وارد شده در سیستم موجود است");

        if(dto.getGroupCode() != null) {
            groupRepository.findByCode(dto.getGroupCode()).orElseThrow(InvalidCodeException::new);
        }

//        PairValue existTokenP = existSMS(jsonObject.getString("username"));
//
//        if (existTokenP != null)
//            return generateSuccessMsg("token", existTokenP.getKey(),
//                    new PairValue("reminder", existTokenP.getValue())
//            );

        User user = userRepository.insert(populateEntity(null, dto));
        // todo : send activation code
        return generateSuccessMsg("id", user.getId());
    }


    public String groupStore(GroupSignUpData dto) {

        if(userRepository.countByPhone(dto.getPhone()) > 0)
            return generateErr("شماره همراه وارد شده در سیستم موجود است");

        if(userRepository.countByNID(dto.getNid()) > 0)
            return generateErr("کد ملی وارد شده در سیستم موجود است");

//        PairValue existTokenP = existSMS(jsonObject.getString("username"));
//
//        if (existTokenP != null)
//            return generateSuccessMsg("token", existTokenP.getKey(),
//                    new PairValue("reminder", existTokenP.getValue())
//            );

        User user = userRepository.insert(populateEntity(null, dto));
        // todo : send activation code
        return generateSuccessMsg("id", user.getId());
    }

    public String forgetPass(String NID) {

        if (!Utility.validationNationalCode(NID))
            return JSON_NOT_VALID_PARAMS;

        Optional<User> user = userRepository.findByNID(NID);
        if(user.isEmpty())
            return generateErr("کد ملی وارد شده در سامانه موجود نمی باشد");

        return sendSMS(NID, user.get().getPhone());
    }

    private String sendSMS(String NID, String phone) {

        PairValue existTokenP = existSMS(phone);

        if (existTokenP != null)
            return generateSuccessMsg("token", existTokenP.getKey(),
                    new PairValue("reminder", existTokenP.getValue())
            );

        String token = sendNewSMS(NID, phone);

        return generateSuccessMsg("token", token,
                new PairValue("reminder", SMS_RESEND_SEC)
        );

    }

    private String sendNewSMS(String NID, String phone) {

        int code = Utility.randInt();
        String token = Utility.randomString(20);
        long now = System.currentTimeMillis();

        new Thread(() -> {

            activationRepository.deleteByPhone(phone, now);

            Activation activation = new Activation();
            activation.setCode(code);
            activation.setToken(token);
            activation.setCreatedAt(now);
            activation.setPhone(phone);
            activation.setNid(NID);

            activationRepository.insert(activation);

            Utility.sendSMS(phone, code + "" , "", "", "activationCode");

        }).start();

        return token;
    }

    public PairValue existSMS(String phone) {
        Optional<Activation> activation = activationRepository.findByPhone(phone, System.currentTimeMillis() - SMS_RESEND_MSEC);
        return activation.map(value -> new PairValue(value.getToken(), SMS_RESEND_SEC - (System.currentTimeMillis() - value.getCreatedAt()) / 1000)).orElse(null);
    }

    @Override
    User populateEntity(User user, SignUpData userData) {

        boolean isNew = false;

        if(user == null) {
            user = new User();
            isNew = true;
        }

        user.setName(userData.getName());
        user.setBloodType(userData.getBloodType());
        user.setPhone(userData.getPhone());
        user.setNid(userData.getNid());
        user.setFatherName(userData.getFatherName());
        user.setBirthDay(userData.getBirthDay());
        user.setField(userData.getField());
        user.setUniversity(userData.getUniversity());
        user.setUniversityYear(userData.getUniversityYear());

        if(userData.getDiseases() != null)
            user.setDiseases(userData.getDiseases());

        if(userData.getAllergies() != null)
            user.setAllergies(userData.getAllergies());

        if(userData.getAbilities() != null)
            user.setAbilities(userData.getAbilities());

        user.setSex(userData.getSex());
        user.setNearbyName(userData.getNearbyName());
        user.setNearbyPhone(userData.getNearbyPhone());

        if(isNew) {

            if(userData.getGroupCode() != null) {
                Group group = groupRepository.findByCode(userData.getGroupCode()).orElseThrow(InvalidCodeException::new);
                user.setGroupId(group.getId());
            }

            user.setPassword(getEncPass(userData.getPassword()));
            user.setAccesses(new ArrayList<>() {{
                add(Access.JAHADI);
            }});
            user.setStatus(AccountStatus.PENDING);
        }

        return user;
    }

    User populateEntity(User user, GroupSignUpData userData) {

        boolean isNew = false;

        if(user == null) {
            user = new User();
            isNew = true;
        }

        user.setName(userData.getName());
        user.setPhone(userData.getPhone());
        user.setNid(userData.getNid());
        user.setFatherName(userData.getFatherName());
        user.setBirthDay(userData.getBirthDay());
        user.setField(userData.getField());
        user.setUniversity(userData.getUniversity());
        user.setUniversityYear(userData.getUniversityYear());
        user.setSex(userData.getSex());

        user.setGroupName(userData.getGroupName());
        user.setTrips(userData.getTrips());
        user.setMembers(userData.getMembers());
        user.setOrganizationDependency(userData.getOrganizationDependency());
        user.setFamiliarWith(userData.getFamiliarWith());

        if(isNew) {

            user.setPassword(getEncPass(userData.getPassword()));
            user.setAccesses(new ArrayList<>() {{
                add(Access.JAHADI);
            }});
            user.setStatus(AccountStatus.PENDING);
        }

        return user;
    }

    @Override
    public ResponseEntity<User> findById(ObjectId id, Object ... params) {

        return new ResponseEntity<>(
                userRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );

    }

    public String checkCode(CheckCodeRequest checkCodeRequest) {

        if (!Utility.validationNationalCode(checkCodeRequest.getNid()))
            return JSON_NOT_VALID_PARAMS;

        if (checkCodeRequest.getToken().length() != 20)
            return JSON_NOT_VALID_PARAMS;

        if (checkCodeRequest.getCode() < 100000 || checkCodeRequest.getCode() > 999999)
            return Utility.generateErr("کد وارد شده معتبر نمی باشد.");

        Optional<Activation> activation = activationRepository.findByNidAndCodeAndToken(
                checkCodeRequest.getNid(), checkCodeRequest.getCode(), checkCodeRequest.getToken()
        );

        if (activation.isEmpty())
            return generateErr("کد وارد شده معتبر نیست");

        if(activation.get().getCreatedAt() < System.currentTimeMillis() - SMS_RESEND_MSEC)
            return generateErr("کد موردنظر شما منقضی شده است");

        return JSON_OK;
    }

    public String resetPassword(ResetPasswordRequest request) {

        if (!Utility.validationNationalCode(request.getNid()))
            return JSON_NOT_VALID_PARAMS;

        if (request.getToken().length() != 20)
            return JSON_NOT_VALID_PARAMS;

        if (request.getCode() < 100000 || request.getCode() > 999999)
            return generateErr("کد وارد شده معتبر نمی باشد.");

        if (!request.getPassword().equals(request.getRepeatPassword()))
            return generateErr("رمزجدید و تکرار آن یکسان نیستند.");

        if (request.getPassword().length() < 6)
            return generateErr("رمزجدید انتخاب شده قوی نیست.");

        Optional<Activation> activation = activationRepository.findByNidAndCodeAndToken(request.getNid(),
                request.getCode(), request.getToken()
        );

        if (activation.isEmpty())
            return generateErr("کد وارد شده معتبر نیست");

        if (activation.get().getCreatedAt() < System.currentTimeMillis() - SMS_VALIDATION_EXPIRATION_MSEC_LONG)
            return generateErr("توکن موردنظر منقضی شده است.");

        activationRepository.delete(activation.get());

        Optional<User> user = userRepository.findByNID(activation.get().getNid());
        if(user.isEmpty())
            return JSON_NOT_UNKNOWN;

        User u = user.get();
        u.setPassword(getEncPass(request.getPassword()));
        userRepository.save(u);

        return JSON_OK;
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

    public String toggleStatus(ObjectId userId) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            return JSON_NOT_VALID_ID;

        User u = user.get();

        switch (u.getStatus()) {
            case ACTIVE:
                u.setStatus(AccountStatus.BLOCKED);
                break;
            case PENDING:
            case BLOCKED:
                u.setStatus(AccountStatus.ACTIVE);
                break;
            default:
                return JSON_NOT_VALID_PARAMS;
        }

        userRepository.save(u);

        return Utility.generateSuccessMsg("newStatus", u.getStatus().getName());
    }

    public ResponseEntity<String> signIn(SignInData data) {

        try {

            if(!DEV_MODE) {

                for (int i = 0; i < cachedToken.size(); i++) {
                    if (cachedToken.get(i).equals(data)) {
                        if (cachedToken.get(i).checkExpiration())
                            return new ResponseEntity<>(
                                    (String) cachedToken.get(i).getValue(),
                                    HttpStatus.OK
                            );

                        cachedToken.remove(i);
                        break;
                    }
                }
            }

            Optional<User> user = userRepository.findByNID(data.getNid());

            if(user.isEmpty() || user.get().getRemoveAt() != null)
                return new ResponseEntity<>(
                        "نام کاربری و یا رمزعبور اشتباه است.",
                        HttpStatus.BAD_REQUEST
                );

            if (!DEV_MODE) {
                if (!passwordEncoder.matches(data.getPassword(), user.get().getPassword()))
                    return new ResponseEntity<>(
                            "نام کاربری و یا رمزعبور اشتباه است.",
                            HttpStatus.BAD_REQUEST
                    );
            }

            if (!user.get().getStatus().equals(AccountStatus.ACTIVE))
                return new ResponseEntity<>(
                        "اکانت شما غیرفعال می باشد.",
                        HttpStatus.BAD_REQUEST
                );

            String token = jwtTokenProvider.createToken(data.getNid(), user.get().getAccesses());

            if(!DEV_MODE)
                cachedToken.add(new Cache(TOKEN_EXPIRATION, token, data));

            return new ResponseEntity<>(
                    token, HttpStatus.OK
            );

        } catch (AuthenticationException x) {
            return new ResponseEntity<>(
                    "نام کاربری و یا رمزعبور اشتباه است.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void logout(String token) {

        for (int i = 0; i < cachedToken.size(); i++) {
            if (cachedToken.get(i).getValue().equals(token)) {
                cachedToken.remove(i);
                return;
            }
        }

        jwtTokenProvider.removeTokenFromCache(token.replace("Bearer ", ""));

    }

    public User whoAmI(HttpServletRequest req) {
        try {

            Optional<User> u = userRepository.findByNID(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));

            if(u.isEmpty() || u.get().getRemoveAt() != null)
                return null;

            return u.get();
        }
        catch (Exception x) {
            return null;
        }
    }

    public void changeStatus(ObjectId userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setStatus(status);
        userRepository.save(user);
    }

    public void changePassword(ObjectId userId, PasswordData passwordData) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setPassword(passwordEncoder.encode(passwordData.getPassword()));
        userRepository.save(user);
    }

    public void setPic(ObjectId id, MultipartFile file) {

        if(file == null)
            throw new BadRequestException();

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        String picPath = "ad";

        user.setPic(picPath);
        userRepository.save(user);

    }

    public void setGroup(ObjectId id, Integer code) {

        Group group = groupRepository.findByCode(code).orElseThrow(InvalidCodeException::new);

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        user.setGroupId(group.getId());
        user.setGroupName(group.getName());
        userRepository.save(user);

    }
}
