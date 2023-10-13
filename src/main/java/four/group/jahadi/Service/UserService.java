package four.group.jahadi.Service;

import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.*;
import four.group.jahadi.Models.Activation;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ActivationRepository;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Utility.Cache;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static four.group.jahadi.Utility.StaticValues.*;
import static four.group.jahadi.Utility.Utility.*;
import static org.springframework.beans.BeanUtils.copyProperties;


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
                        filters[6] != null ? filters[6].toString() : null,
                        filters[7] != null ? (ObjectId) filters[7] : null,
                        filters[8] != null ? (Boolean) filters[8] : null
                ),
                HttpStatus.OK
        );
    }

    @Override
    public void update(ObjectId id, SignUpData dto, Object... params) {

    }

    public void update(ObjectId id, UpdateInfoData dto) {

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (dto.getName() != null)
            user.setName(dto.getName());

        if (dto.getBirthDay() != null)
            user.setBirthDay(dto.getBirthDay());

        if (dto.getFatherName() != null)
            user.setFatherName(dto.getFatherName());

        if (dto.getField() != null)
            user.setField(dto.getField());

        if (dto.getUniversity() != null)
            user.setUniversity(dto.getUniversity());

        if (dto.getUniversityYear() != null)
            user.setUniversityYear(dto.getUniversityYear());

        if (dto.getNearbyName() != null)
            user.setNearbyName(dto.getNearbyName());

        if (dto.getNearbyPhone() != null)
            user.setNearbyPhone(dto.getNearbyPhone());

        if (dto.getAllergies() != null)
            user.setAllergies(dto.getAllergies());

        if (dto.getDiseases() != null)
            user.setDiseases(dto.getDiseases());

        if (dto.getAbilities() != null)
            user.setAbilities(dto.getAbilities());

        if (dto.getSex() != null)
            user.setSex(dto.getSex());

        if (dto.getBloodType() != null)
            user.setBloodType(dto.getBloodType());

        userRepository.save(user);
    }


    public ResponseEntity<HashMap<String, Object>> checkUniqueness(SignUpStep1Data dto) {

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        if (userRepository.countByNID(dto.getNid()) > 0)
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        User user = new User();
        copyProperties(dto, user);

        return sendSMS(user);
    }

    public void checkGroup(SignUpStep3Data dto) {
        if (groupRepository.countByCode(dto.getGroupCode()) == 0)
            throw new InvalidFieldsException("کد گروه نامعتبر است");
    }

    public ResponseEntity<String> signUp(SignUpData dto) {

        Group group = null;

        if (dto.getGroupCode() != null)
            group = groupRepository.findByCode(dto.getGroupCode()).orElseThrow(InvalidCodeException::new);

        Activation activation = activationRepository.findByPhone(dto.getPhone()).orElseThrow(NotAccessException::new);

        User user = activation.getUser();

        if(group != null) {
            user.setGroupId(group.getId());
            user.setGroupName(group.getName());
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(AccountStatus.PENDING);
        user.setAccesses(Collections.singletonList(Access.JAHADI));

        activationRepository.delete(activation);
        userRepository.insert(user);

        return new ResponseEntity<>(
                jwtTokenProvider.createToken(user.getNid(), user.getAccesses(), user.getGroupId(), user.getId()),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<User> store(SignUpData dto, Object... params) {

//        if (userRepository.countByPhone(dto.getPhone()) > 0)
//            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");
//
//        if (userRepository.countByNID(dto.getNid()) > 0)
//            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");
//
//        if (dto.getGroupCode() != null) {
//            groupRepository.findByCode(dto.getGroupCode()).orElseThrow(InvalidCodeException::new);
//        }
//
//        User user = userRepository.insert(populateEntity(null, dto));
//        return new ResponseEntity<>(user, HttpStatus.OK);

        return null;
    }

    public ResponseEntity<HashMap<String, Object>> forgetPass(String NID) {

        if (!Utility.validationNationalCode(NID))
            throw new InvalidFieldsException("کد ملی وارد شده معتبر نمی باشد");

        User user = userRepository.findByNID(NID).orElseThrow(
                () -> new InvalidFieldsException("کد ملی وارد شده در سامانه موجود نمی باشد")
        );

        return sendSMS(user);
    }

    private ResponseEntity<HashMap<String, Object>> sendSMS(User user) {

        PairValue existTokenP = existSMS(user.getPhone());
        HashMap<String, Object> output = new HashMap<>();

        if (existTokenP != null) {
            output.put("token", existTokenP.getKey().toString());
            output.put("reminder", existTokenP.getValue());
        }
        else {
            String token = sendNewSMS(user);
            output.put("token", token);
            output.put("reminder", SMS_RESEND_SEC);
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private String sendNewSMS(User user) {

        int code = Utility.randInt();
        String token = Utility.randomString(20);
        long now = System.currentTimeMillis();

        new Thread(() -> {

            activationRepository.deleteByPhone(user.getPhone(), now);

            Activation activation = Activation.builder()
                    .phone(user.getPhone())
                    .user(user)
                    .token(token)
                    .code(code)
                    .createdAt(now)
                    .build();

            activationRepository.insert(activation);

            Utility.sendSMS(user.getPhone(), code + "", "", "", "activation");

        }).start();

        return token;
    }

    public PairValue existSMS(String phone) {
        Optional<Activation> activation = activationRepository.findByPhone(phone, System.currentTimeMillis() - SMS_RESEND_MSEC);
        return activation.map(value -> new PairValue(value.getToken(), SMS_RESEND_SEC - (System.currentTimeMillis() - value.getCreatedAt()) / 1000)).orElse(null);
    }

    User populateEntity(SignUpStep1ForGroupData userData) {

        User user = new User();

        user.setName(userData.getName());
        user.setPhone(userData.getPhone());
        user.setNid(userData.getNid());
        user.setFatherName(userData.getFatherName());
        user.setBirthDay(userData.getBirthDay());
        user.setField(userData.getField());
        user.setUniversity(userData.getUniversity());
        user.setUniversityYear(userData.getUniversityYear());
        user.setSex(userData.getSex());
        user.setEndManageYear(userData.getEndManageYear());
        user.setCid(userData.getCid());

        user.setPassword(getEncPass(userData.getPassword()));
        user.setAccesses(new ArrayList<>() {{
            add(Access.JAHADI);
        }});
        user.setStatus(AccountStatus.PENDING);

        return user;
    }

    @Override
    public ResponseEntity<User> findById(ObjectId id, Object... params) {

        if (id == null)
            throw new BadRequestException();

        return new ResponseEntity<>(
                userRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );

    }

    public ResponseEntity<String> checkCode(CheckCodeRequest checkCodeRequest) {

        Activation activation = activationRepository.findByPhoneAndCodeAndToken(
                checkCodeRequest.getPhone(), checkCodeRequest.getCode(), checkCodeRequest.getToken()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کد وارد شده نامعتبر است");
        });

        if (activation.getCreatedAt() < System.currentTimeMillis() - SMS_RESEND_MSEC)
            throw new InvalidFieldsException("کد موردنظر شما منقضی شده است");

        User user = activation.getUser();

        if(user.getCid() != null) {

            userRepository.insert(user);
            activationRepository.delete(activation);

            return new ResponseEntity<>(
                    jwtTokenProvider.createToken(user.getNid(), user.getAccesses(), user.getGroupId(), user.getId()),
                    HttpStatus.OK
            );
        }
        else {
            activation.setValidated(true);
            activationRepository.save(activation);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public void resetPassword(ResetPasswordRequest request) {

        if (!Utility.validationNationalCode(request.getNid()))
            throw new InvalidFieldsException("کد ملی وارد شده معتبر نمی باشد");

        if (request.getToken().length() != 20)
            throw new InvalidFieldsException("توکن موردنظر معتبر نمی باشد");

        if (request.getCode() < 100000 || request.getCode() > 999999)
            throw new InvalidFieldsException("کد وارد شده معتبر نمی باشد.");

        if (!request.getPassword().equals(request.getRepeatPassword()))
            throw new InvalidFieldsException("رمزجدید و تکرار آن یکسان نیستند.");

        if (request.getPassword().length() < 6)
            throw new InvalidFieldsException("رمزجدید انتخاب شده قوی نیست.");

//        Activation activation = activationRepository.findByPhoneAndCodeAndToken(
//                request.getNid(), request.getCode(), request.getToken()
//        ).orElseThrow(() -> {
//            throw new InvalidFieldsException("کد وارد شده معتبر نیست");
//        });
//
//        if (activation.getCreatedAt() < System.currentTimeMillis() - SMS_VALIDATION_EXPIRATION_MSEC_LONG)
//            throw new InvalidFieldsException("توکن موردنظر منقضی شده است.");
//
//        activationRepository.delete(activation);

//        User user = userRepository.findByPhone(activation.getPhone()).orElseThrow(InvalidIdException::new);
//
//        user.setPassword(getEncPass(request.getPassword()));
//        userRepository.save(user);
    }

    public String toggleStatus(ObjectId userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return JSON_NOT_VALID_ID;

        User u = user.get();

        switch (u.getStatus()) {
            case ACTIVE:
                u.setStatus(AccountStatus.BLOCKED);
                break;
            case PENDING:
            case BLOCKED:
                u.setStatus(AccountStatus.ACTIVE);
                if (u.getMembers() != null) {

                    Optional<Group> group = groupRepository.findByName(u.getGroupName());

                    Group g;

                    if (group.isEmpty()) {

                        int code = Utility.randIntForGroupCode();
                        Optional<Group> tmp = groupRepository.findByCode(code);

                        while (tmp.isPresent())
                            code = Utility.randIntForGroupCode();

                        g = Group.builder()
                                .name(u.getGroupName())
                                .code(code)
                                .build();

                        g.setOwner(u.getId());
                        groupRepository.insert(g);

                        if (!u.getAccesses().contains(Access.GROUP))
                            u.getAccesses().add(Access.GROUP);
                    } else
                        g = group.get();

                    u.setGroupId(g.getId());
                }
                break;
            default:
                return JSON_NOT_VALID_PARAMS;
        }

        userRepository.save(u);

        return Utility.generateSuccessMsg("newStatus", u.getStatus().getName());
    }

    public ResponseEntity<String> signIn(SignInData data) {

        try {

            if (!DEV_MODE) {

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

            if (user.isEmpty() || user.get().getRemoveAt() != null)
                return new ResponseEntity<>(
                        "نام کاربری و یا رمزعبور اشتباه است.",
                        HttpStatus.BAD_REQUEST
                );

            User u = user.get();

            if (!DEV_MODE) {
                if (!passwordEncoder.matches(data.getPassword(), u.getPassword()))
                    return new ResponseEntity<>(
                            "نام کاربری و یا رمزعبور اشتباه است.",
                            HttpStatus.BAD_REQUEST
                    );
            }

            if (!u.getStatus().equals(AccountStatus.ACTIVE))
                return new ResponseEntity<>(
                        "اکانت شما غیرفعال می باشد.",
                        HttpStatus.BAD_REQUEST
                );

            String token = jwtTokenProvider.createToken(data.getNid(), u.getAccesses(), u.getGroupId(), u.getId());

            if (!DEV_MODE)
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

            if (u.isEmpty() || u.get().getRemoveAt() != null)
                return null;

            return u.get();
        } catch (Exception x) {
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

        if (file == null)
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




    public ResponseEntity<HashMap<String, Object>> groupStore(SignUpStep1ForGroupData dto) {

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        if (userRepository.countByNID(dto.getNid()) > 0)
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        return sendSMS(populateEntity(dto));
    }

    public void signUpStep2ForGroups(User user, SignUpStep2ForGroupData dto) {

        if(groupRepository.findByName(dto.getGroupName()).isPresent())
            throw new InvalidFieldsException("نام گروه جهادی در سیستم موجود است");

        copyProperties(dto, user);
        userRepository.save(user);
    }

    public void signUpStep3ForGroups(User user, SignUpStep3ForGroupData dto) {
        copyProperties(dto, user);
        userRepository.save(user);
    }

    public void signUpStep4ForGroups(User user, SignUpStep4ForGroupData dto) {
        copyProperties(dto, user);
        userRepository.save(user);
    }

}
