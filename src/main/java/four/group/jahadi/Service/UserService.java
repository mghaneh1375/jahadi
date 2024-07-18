package four.group.jahadi.Service;

import four.group.jahadi.DTO.AdminSignInData;
import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.*;
import four.group.jahadi.Models.Activation;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.ActivationRepository;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Utility.*;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static four.group.jahadi.Utility.FileUtils.removeFile;
import static four.group.jahadi.Utility.FileUtils.uploadFile;
import static four.group.jahadi.Utility.StaticValues.*;
import static four.group.jahadi.Utility.Utility.convertPersianDigits;
import static org.springframework.beans.BeanUtils.copyProperties;


@Service
public class UserService extends AbstractService<User, SignUpData> {

    private static ArrayList<Cache> cachedToken = new ArrayList<>();
    public final static String PICS_FOLDER = "userPics";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivationRepository activationRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String getEncPass(String pass) {
        return passwordEncoder.encode(convertPersianDigits(pass));
    }

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
    public void update(ObjectId id, SignUpData dto, Object... params) {

    }

    public void update(ObjectId id, UpdateInfoData dto) {

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
        try {
            notNull.copyProperties(user, dto);
            userRepository.save(user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<HashMap<String, Object>> checkUniqueness(SignUpStep1Data dto) {

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        if (userRepository.countByNID(dto.getNid()) > 0)
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        User user = new User();
        copyProperties(dto, user);

        return sendSMS(user, true);
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

        if (group != null) {
            user.setGroupId(group.getId());
            user.setGroupName(group.getName());
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(AccountStatus.PENDING);
        user.setAccesses(Collections.singletonList(Access.JAHADI));

        user.setNearbyName(dto.getNearbyName());
        user.setNearbyPhone(dto.getNearbyPhone());
        user.setNearbyRel(dto.getNearbyRel());
        user.setAllergies(dto.getAllergies());
        user.setDiseases(dto.getDiseases());
        user.setAbilities(dto.getAbilities());
        user.setBloodType(dto.getBloodType());

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

        return sendSMS(user, false);
    }

    private ResponseEntity<HashMap<String, Object>> sendSMS(User user, boolean storeUserDoc) {

        PairValue existTokenP = existSMS(user.getPhone());
        HashMap<String, Object> output = new HashMap<>();

        if (existTokenP != null) {
            output.put("token", existTokenP.getKey().toString());
            output.put("reminder", existTokenP.getValue());
        } else {
            String token = sendNewSMS(user, storeUserDoc);
            output.put("token", token);
            output.put("reminder", SMS_RESEND_SEC);
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private String sendNewSMS(User user, boolean storeUserDoc) {

        int code = Utility.randInt();
        String token = Utility.randomString(20);
        long now = System.currentTimeMillis();

        new Thread(() -> {

            activationRepository.deleteByPhone(user.getPhone(), now);

            Activation activation = Activation.builder()
                    .token(token)
                    .code(code)
                    .createdAt(now)
                    .build();

            if (storeUserDoc) {
                activation.setUser(user);
                activation.setPhone(user.getPhone());
            } else
                activation.setNid(user.getNid());

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

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        if(params.length > 0) {
            if (!Objects.equals(params[0], user.getGroupId()))
                throw new NotAccessException();
        }

        user.getAccesses().stream().filter(access -> access.equals(Access.GROUP))
                .findFirst().flatMap(access -> groupRepository.findById(user.getGroupId()))
                .ifPresent(group -> {
                    user.setGroupCode(group.getCode());
                    user.setGroupPic(group.getPic());
                });

        return new ResponseEntity<>(
                user,
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

        if (user.getCid() != null && userRepository.countByNID(user.getNid()) == 0) {

            userRepository.insert(user);
            activationRepository.delete(activation);

            return new ResponseEntity<>(
                    jwtTokenProvider.createToken(user.getNid(), user.getAccesses(), user.getGroupId(), user.getId()),
                    HttpStatus.OK
            );
        } else {
            activation.setValidated(true);
            activationRepository.save(activation);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }


    public ResponseEntity<String> checkForgetPassCode(CheckForgetPassCodeRequest checkCodeRequest) {

        Activation activation = activationRepository.findByNIDAndCodeAndToken(
                checkCodeRequest.getNid(), checkCodeRequest.getCode(), checkCodeRequest.getToken()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کد وارد شده نامعتبر است");
        });

        if (activation.getCreatedAt() < System.currentTimeMillis() - SMS_RESEND_MSEC)
            throw new InvalidFieldsException("کد موردنظر شما منقضی شده است");

        activation.setValidated(true);
        activationRepository.save(activation);

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public void resetPassword(ResetPasswordRequest request) {

        if (!Utility.validationNationalCode(request.getNid()))
            throw new InvalidFieldsException("کد ملی وارد شده معتبر نمی باشد");

        if (request.getToken().length() != 20)
            throw new InvalidFieldsException("توکن موردنظر معتبر نمی باشد");

        if (request.getCode() < 100000 || request.getCode() > 999999)
            throw new InvalidFieldsException("کد وارد شده معتبر نمی باشد.");

        Activation activation =
                activationRepository.findByNIDAndCodeAndToken(request.getNid(), request.getCode(), request.getToken())
                        .orElseThrow(() -> {
                            throw new InvalidFieldsException("کد وارد شده نامعتبر است");
                        });

        if (!activation.getValidated())
            throw new NotAccessException();

        if (!request.getPassword().equals(request.getRepeatPassword()))
            throw new InvalidFieldsException("رمزجدید و تکرار آن یکسان نیستند.");

        if (request.getPassword().length() < 6)
            throw new InvalidFieldsException("رمزجدید انتخاب شده قوی نیست.");

        activationRepository.delete(activation);

        User user = userRepository.findByNID(activation.getNid()).orElseThrow(InvalidIdException::new);
        user.setPassword(getEncPass(request.getPassword()));
        userRepository.save(user);
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
                throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");

            User u = user.get();

            if (!DEV_MODE) {
                if (!passwordEncoder.matches(data.getPassword(), u.getPassword()))
                    throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");
            }

            if (!u.getStatus().equals(AccountStatus.ACTIVE))
                throw new InvalidFieldsException("اکانت شما غیرفعال می باشد.");

            String token = jwtTokenProvider.createToken(data.getNid(), u.getAccesses(), u.getGroupId(), u.getId());

            if (!DEV_MODE)
                cachedToken.add(new Cache(TOKEN_EXPIRATION, token, data));

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

            if(!u.getGroupId().equals(groupId))
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

        if (file.getSize() > ONE_MB * 5)
            throw new RuntimeException("حداکثر حجم مجاز 5MB می باشد");

        String fileType = FileUtils.uploadImage(file);

        if (fileType == null)
            throw new RuntimeException("فرمت فایل موردنظر معتبر نمی باشد.");

        String filename = uploadFile(file, PICS_FOLDER);
        if (filename == null)
            throw new RuntimeException("خطای ناشناخته هنگام بارگداری فایل");

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (user.getPic() != null && !user.getPic().isEmpty())
            removeFile(user.getPic(), PICS_FOLDER);

        user.setPic(filename);
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

        return sendSMS(populateEntity(dto), true);
    }

    public void signUpStep2ForGroups(User user, SignUpStep2ForGroupData dto) {

        if (groupRepository.findByName(dto.getGroupName()).isPresent())
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

    public ResponseEntity<List<User>> findGroupMembersByRegionOwner(ObjectId userId, ObjectId groupId) {

        List<Trip> trips =
                tripRepository.findActivesOrNotStartedProjectIdsByAreaOwnerId(Utility.getCurrDate(), userId);

        if (trips.size() == 0)
            throw new NotAccessException();

        return new ResponseEntity<>(
                userRepository.findAll(
                        AccountStatus.ACTIVE, Access.JAHADI,
                        null, null, null, null,
                        null, groupId, null
                ),
                HttpStatus.OK
        );
    }

    public void remove(ObjectId userId) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setDeletedAt(new Date());
        userRepository.save(user);
    }

    public ResponseEntity<User> info(ObjectId userId) {

        User user = userRepository.findDigestById(userId).orElseThrow(InvalidIdException::new);

        user.setRole(user.getAccesses().contains(Access.ADMIN) ? Access.ADMIN :
                user.getAccesses().contains(Access.GROUP) ? Access.GROUP : Access.JAHADI
        );

        if(Objects.equals(user.getRole(), Access.JAHADI)) {
            Date currDate = Utility.getCurrDate();
            user.setHasActiveRegion(tripRepository.existNotFinishedByAreaOwnerId(currDate, userId));
            user.setHasActiveTask(tripRepository.existNotFinishedByResponsibleId(currDate, userId));
        }

        return new ResponseEntity<>(
                user,
                HttpStatus.OK
        );
    }
}
