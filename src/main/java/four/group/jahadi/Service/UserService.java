package four.group.jahadi.Service;

import four.group.jahadi.DTO.ChangePhoneDAO;
import four.group.jahadi.DTO.ChangePhoneResponseDAO;
import four.group.jahadi.DTO.Digest.MyAccesses;
import four.group.jahadi.DTO.DoChangePhoneDAO;
import four.group.jahadi.DTO.SignUp.*;
import four.group.jahadi.DTO.UserDigest;
import four.group.jahadi.DTO.reporter.CreateReporterUser;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Enums.AccountStatus;
import four.group.jahadi.Enums.Color;
import four.group.jahadi.Enums.Sex;
import four.group.jahadi.Exception.*;
import four.group.jahadi.Models.*;
import four.group.jahadi.Repository.ActivationRepository;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Repository.impl.UserCustomRepositoryImpl;
import four.group.jahadi.Security.JwtTokenFilter;
import four.group.jahadi.Security.JwtTokenProvider;
import four.group.jahadi.Utility.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static four.group.jahadi.Service.Area.ReportUtil.prepareHttpServletResponse;
import static four.group.jahadi.Utility.FileUtils.removeFile;
import static four.group.jahadi.Utility.FileUtils.uploadFile;
import static four.group.jahadi.Utility.StaticValues.*;
import static four.group.jahadi.Utility.Utility.convertPersianDigits;
import static org.springframework.beans.BeanUtils.copyProperties;


@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User, SignUpData> {

    private static final ArrayList<Cache> cachedToken = new ArrayList<>();
    public final static String PICS_FOLDER = "userPics";
    private final UserRepository userRepository;
    private final ActivationRepository activationRepository;
    private final GroupRepository groupRepository;
    private final TripRepository tripRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExcelService excelService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserCustomRepositoryImpl userCustomRepository;
    private final GroupReportService groupReportService;
    private final WareHouseAccessService wareHouseAccessService;
    private final ExternalReferralAccessForGroupService externalReferralAccessForGroupService;
    private final TripService tripService;

    @Value("${custom.application.mode}")
    private String appMode;

    private final static List<String> USERS_EXCEL_COLS = List.of(
            "نام مسئول",
            "جنسیت",
            "نام پدر",
            "گروه",
            "کد ملی",
            "دانشگاه",
            "شماره تلفن",
            "تاریخ تولد",
            "رشته تحصیلی"
    );

    public String getEncPass(String pass) {
        return passwordEncoder.encode(convertPersianDigits(pass));
    }

    @Override
    public ResponseEntity<List<User>> list(Object... filters) {
        return null;
    }

    @Override
    public ResponseEntity<Page<User>> paginateList(int pageIndex, int pageSize, Object... filters) {
        Page<User> users = userCustomRepository.findAdvanced(
                (AccountStatus) filters[0], (Access) filters[1],
                filters[2] != null ? filters[2].toString() : null,
                filters[3] != null ? filters[3].toString() : null,
                filters[4] != null ? filters[4].toString() : null,
                filters[5] != null ? (Sex) filters[5] : null,
                filters[6] != null ? filters[6].toString() : null,
                filters[7] != null ? (ObjectId) filters[7] : null,
                filters[8] != null ? (Boolean) filters[8] : null,
                filters[9] != null ? filters[9].toString() : null,
                Pageable.ofSize(pageSize).withPage(pageIndex),
                new String[] {"_id", "name", "NID", "phone", "group_name", "father_name", "university", "birth_day", "status", "access", "old_trips_count", "field", "tel", "group_id"}
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

    public void excelReport(HttpServletResponse response, Object... filters) {
        Page<User> users = userCustomRepository.findAdvanced(
                (AccountStatus) filters[0], (Access) filters[1],
                filters[2] != null ? filters[2].toString() : null,
                filters[3] != null ? filters[3].toString() : null,
                filters[4] != null ? filters[4].toString() : null,
                filters[5] != null ? (Sex) filters[5] : null,
                filters[6] != null ? filters[6].toString() : null,
                filters[7] != null ? (ObjectId) filters[7] : null,
                filters[8] != null ? (Boolean) filters[8] : null,
                filters[9] != null ? filters[9].toString() : null,
                Pageable.ofSize(Integer.MAX_VALUE).withPage(0),
                new String[] {"_id", "name", "NID", "phone", "group_name", "father_name", "university", "birth_day", "status", "access", "sex", "field"}
        );

        Workbook workbook = excelService.createExcel(Collections.singletonList("کاربران"));
        Sheet sheet = workbook.getSheetAt(0);
        excelService.writeExcelHeader(sheet, USERS_EXCEL_COLS);

        AtomicInteger atomicInteger = new AtomicInteger(1);
        users.getContent().forEach(user -> {
            Row row = sheet.createRow(atomicInteger.getAndIncrement());
            int col = 0;
            Cell cell = row.createCell(col++);
            cell.setCellValue(user.getName());
            cell = row.createCell(col++);
            cell.setCellValue(user.getSex() == null ? "" : user.getSex().getFaTranslate());
            cell = row.createCell(col++);
            cell.setCellValue(user.getFatherName());
            cell = row.createCell(col++);
            cell.setCellValue(user.getGroupName());
            cell = row.createCell(col++);
            cell.setCellValue(user.getNid());
            cell = row.createCell(col++);
            cell.setCellValue(user.getUniversity());
            cell = row.createCell(col++);
            cell.setCellValue(user.getPhone());
            cell = row.createCell(col++);
            cell.setCellValue(user.getBirthDay());
            cell = row.createCell(col++);
            cell.setCellValue(user.getField());
        });

        prepareHttpServletResponse(response, workbook, "users");
    }

    @Override
    public void update(ObjectId id, SignUpData dto, Object... params) {

    }

    @CacheEvict(value = "user", key = "#userId")
    public void update(ObjectId userId, UpdateInfoData dto) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
        if (user.getAccesses().contains(Access.GROUP)) {
            try {
                notNull.copyProperties(user, dto);
                userRepository.save(user);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                PersonalUpdateInfoData personalUpdateInfoData = new PersonalUpdateInfoData();
                notNull.copyProperties(personalUpdateInfoData, dto);
                notNull.copyProperties(user, personalUpdateInfoData);
                userRepository.save(user);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseEntity<ChangePhoneResponseDAO> changePhone(User user, ChangePhoneDAO request) {
        if (!user.getStatus().equals(AccountStatus.ACTIVE))
            throw new NotAccessException();

        if (userRepository.countByPhone(request.getNewPhone()) > 0)
            throw new InvalidFieldsException("این شماره در سیستم موجود است");

        long curr = System.currentTimeMillis();
        activationRepository.findByPhone(request.getNewPhone()).ifPresent(activation -> {
            if ((curr - activation.getCreatedAt()) / 60000 > 2)
                activationRepository.deleteByPhone(request.getNewPhone(), curr);
            throw new InvalidFieldsException("کد قبلی هنوز منقضی نشده است");
        });

        String token = Utility.randomString(20);
        Integer code = Utility.randInt(appMode);
        Utility.sendSMS(appMode, user.getPhone(), code + "", "", "", "activation");
        activationRepository.save(
                Activation
                        .builder()
                        .phone(request.getNewPhone())
                        .token(token)
                        .code(code)
                        .nid(user.getPhone())
                        .createdAt(curr)
                        .build()
        );

        return new ResponseEntity<>(
                ChangePhoneResponseDAO
                        .builder()
                        .token(token)
                        .build(),
                HttpStatus.OK
        );
    }

    public void doChangePhone(User user, DoChangePhoneDAO request, String token) {
        Activation activation = activationRepository.findByNIDAndCodeAndToken(
                user.getPhone(), request.getCode(), request.getToken()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کد وارد شده اشتباه است");
        });

        if (((System.currentTimeMillis() - activation.getCreatedAt()) / 60000) > 2)
            throw new InvalidFieldsException("زمان کد ارسال شده منقضی شده است");

        user.setPhone(activation.getPhone());
        activationRepository.delete(activation);

        logout(token);
        JwtTokenFilter.removeTokenFromCache(token.replace("Bearer ", ""));
        userRepository.save(user);
    }

    public ResponseEntity checkUniqueness(UniquenessValidatorData dto) {

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        if (userRepository.countByNID(dto.getNid()) > 0)
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<HashMap<String, Object>> checkPhone(PersonSignUpCheckPhoneData dto) {
        if (userRepository.countActivesByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        User user = new User();
        user.setPhone(dto.getPhone());

        return sendSMS(user, false);
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

    public ResponseEntity<String> newSignUp(PersonalSignUpData dto) {

        Activation activation = activationRepository.findByPhone(dto.getPhone()).orElseThrow(NotAccessException::new);
        if (!activation.getValidated() || !activation.getToken().equals(dto.getToken()))
            throw new NotAccessException();

        if (activation.getCreatedAt() < System.currentTimeMillis() - ONE_MIN_MSEC * 10)
            throw new InvalidFieldsException("از زمان وارد کردن کد تاییده بیش از 10 دقیقه سپری شده و فرآیند مجاز نمی باشد. لطفا مجدد ثبت نام کنید");

        if (userRepository.countByNID(dto.getNid()) > 0)
            throw new InvalidFieldsException("کدملی وارد شده در سامانه موجود است");

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سامانه موجود است");

        Group group = null;
        if (dto.getGroupCode() != null)
            group = groupRepository.findByCode(dto.getGroupCode()).orElseThrow(InvalidCodeException::new);

        User user = new User();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setNid(dto.getNid());
        user.setFatherName(dto.getFatherName());
        user.setBirthDay(dto.getBirthDay());
        user.setField(dto.getField());
        user.setUniversity(dto.getUniversity());
        user.setUniversityYear(dto.getUniversityYear());
        user.setSex(dto.getSex());
        user.setPassword(getEncPass(dto.getPassword()));
        user.setAccesses(new ArrayList<>() {{
            add(Access.JAHADI);
        }});
        user.setStatus(AccountStatus.PENDING);

        if (group != null) {
            user.setGroupId(group.getId());
            user.setGroupName(group.getName());
        }

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

    synchronized
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
        int code = Utility.randInt(appMode);
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
            } else {
                if (user.getNid() != null)
                    activation.setNid(user.getNid());
                else if (user.getPhone() != null)
                    activation.setPhone(user.getPhone());
            }

            activationRepository.insert(activation);
            Utility.sendSMS(appMode, user.getPhone(), code + "", "", "", "activation");

        }).start();

        return token;
    }

    public PairValue existSMS(String phone) {
        Optional<Activation> activation = activationRepository.findByPhone(phone, System.currentTimeMillis() - SMS_RESEND_MSEC);
        return activation.map(value -> new PairValue(value.getToken(), SMS_RESEND_SEC - (System.currentTimeMillis() - value.getCreatedAt()) / 1000)).orElse(null);
    }

    @Override
    @Cacheable(value = "user", key = "#userId")
    public ResponseEntity<User> findById(ObjectId userId, Object... params) {

        if (userId == null)
            throw new BadRequestException();

        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);

        if (params.length > 0) {
            if (!Objects.equals(params[0], user.getGroupId()))
                throw new NotAccessException();
        }

        if (user.getGroupId() != null) {
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

    public ResponseEntity<String> checkCode(CheckCodeRequest checkCodeRequest) {

        Activation activation = activationRepository.findByPhoneAndCodeAndToken(
                checkCodeRequest.getPhone(), checkCodeRequest.getCode(), checkCodeRequest.getToken()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کد وارد شده نامعتبر است");
        });

        if (activation.getCreatedAt() < System.currentTimeMillis() - SMS_RESEND_MSEC)
            throw new InvalidFieldsException("کد موردنظر شما منقضی شده است");

        User user = activation.getUser();

        if (user != null && user.getCid() != null && userRepository.countByNID(user.getNid()) == 0) {

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

    @CacheEvict(value = "user", key = "#userId")
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
                    u.setGroupName(g.getName());
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
            if (!u.getStatus().equals(AccountStatus.ACTIVE))
                throw new InvalidFieldsException("اکانت شما غیرفعال می باشد.");

            boolean checkPass = Objects.equals(appMode, "release");

            if (
                    u.getTempCode() != null &&
                            u.getTempCodeExp() != null &&
                            u.getTempCodeExp() > System.currentTimeMillis() &&
                            passwordEncoder.matches(data.getPassword(), u.getTempCode())
            )
                checkPass = false;

            if (checkPass &&
                    !passwordEncoder.matches(data.getPassword(), u.getPassword())
            ) {
                throw new InvalidFieldsException("نام کاربری و یا رمزعبور اشتباه است.");
            }

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

    public void createReporterUser(CreateReporterUser dto) {
        if (!dto.getPassword().equals(dto.getPasswordRepeat()))
            throw new InvalidFieldsException("رمزعبور و تکرار آن یکسان نیست");

        if (userRepository.countByNID(dto.getNationalId()) > 0)
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        if (userRepository.countByPhone(dto.getPhone()) > 0)
            throw new InvalidFieldsException("شماره همراه وارد شده در سیستم موجود است");

        User user = User
                .builder()
                .password(passwordEncoder.encode(dto.getPassword()))
                .status(AccountStatus.ACTIVE)
                .fatherName(dto.getFatherName())
                .phone(dto.getPhone())
                .nid(dto.getNationalId())
                .cid(dto.getCid())
                .sex(dto.getSex())
                .accesses(Collections.singletonList(Access.REPORTER))
                .color(Color.BLUE)
                .build();

        userRepository.save(user);
    }

    public ResponseEntity<String> generateTempCode(ObjectId userId) {
        return doGenerateTempCode(
                userRepository.findById(userId).orElseThrow(InvalidIdException::new)
        );
    }

    public ResponseEntity<String> generateTempCode(ObjectId groupId, ObjectId userId) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if(!Objects.equals(user.getGroupId(), groupId))
            throw new NotAccessException();

        return doGenerateTempCode(user);
    }

    private ResponseEntity<String> doGenerateTempCode(User user) {
        String pass = Utility.randomString(24);

        user.setTempCode(passwordEncoder.encode(pass));
        user.setTempCodeExp(System.currentTimeMillis() + ONE_MIN_MSEC * 2);
        userRepository.save(user);

        return ResponseEntity
                .ok()
                .body(pass);
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

    @CacheEvict(value = "user", key = "#userId")
    public void changeStatus(ObjectId userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setStatus(status);
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#userId")
    public void changeStatusByGroup(ObjectId groupId, ObjectId userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if (!Objects.equals(user.getGroupId(), groupId))
            throw new NotAccessException();
        user.setStatus(status);
        userRepository.save(user);
    }

    public void changePassword(ObjectId userId, PasswordData passwordData) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if (!passwordEncoder.matches(passwordData.getCurrPassword(), user.getPassword()))
            throw new InvalidFieldsException("رمزعبور فعلی اشتباه است");
        user.setPassword(passwordEncoder.encode(passwordData.getPassword()));
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#id")
    public void setPic(ObjectId id, MultipartFile file) {

        if (file == null)
            throw new BadRequestException();

        if (file.getSize() > ONE_MB * 5)
            throw new RuntimeException("حداکثر حجم مجاز 5MB می باشد");

        String fileType = FileUtils.uploadImage(file);

        if (fileType == null)
            throw new RuntimeException("فرمت فایل موردنظر معتبر نمی باشد.");

        String filename = uploadFile(appMode, file, PICS_FOLDER);
        if (filename == null)
            throw new RuntimeException("خطای ناشناخته هنگام بارگداری فایل");

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (user.getPic() != null && !user.getPic().isEmpty())
            removeFile(appMode, user.getPic(), PICS_FOLDER);

        user.setPic(filename);
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#id")
    public void setGroup(ObjectId id, Integer code) {
        Group group = groupRepository.findByCode(code).orElseThrow(InvalidCodeException::new);
        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        user.setGroupId(group.getId());
        user.setGroupName(group.getName());
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#userId")
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

    public ResponseEntity<String> groupStore(SignUpStep1ForGroupData dto) {

        Activation activation = activationRepository.findByPhone(dto.getPhone()).orElseThrow(NotAccessException::new);
        if (!activation.getValidated() || !activation.getToken().equals(dto.getToken()))
            throw new NotAccessException();

        if (activation.getCreatedAt() < System.currentTimeMillis() - ONE_MIN_MSEC * 10)
            throw new InvalidFieldsException("از زمان وارد کردن کد تاییده بیش از 10 دقیقه سپری شده و فرآیند مجاز نمی باشد. لطفا مجدد ثبت نام کنید");

        Optional<User> optionalUser = userRepository.findByPhone(dto.getPhone());
        User user = optionalUser.orElseGet(User::new);

        if (
                (user.getNid() == null && userRepository.countByNID(dto.getNid()) > 0) ||
                        (user.getNid() != null && !Objects.equals(userRepository.findIdByNID(dto.getNid()).orElseGet(User::new).getId(), user.getId()))
        )
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        Optional<Group> group = user.getId() != null
                ? groupRepository.findByOwner(user.getId())
                : groupRepository.findByName(dto.getGroupName());

        if (group.isPresent() && (
                user.getId() == null || !group.get().getOwner().equals(user.getId())
        )) {
            throw new InvalidFieldsException("نام گروه جهادی در سیستم موجود است");
        }

        user.setName(dto.getName());

        if (user.getPhone() == null)
            user.setPhone(dto.getPhone());

        user.setNid(dto.getNid());
        user.setCid(dto.getCid());
        user.setFatherName(dto.getFatherName());
        user.setBirthDay(dto.getBirthDay());
        user.setField(dto.getField());
        user.setUniversity(dto.getUniversity());
        user.setUniversityYear(dto.getUniversityYear());
        user.setEndManageYear(dto.getEndManageYear());
        user.setSex(dto.getSex());
        user.setGroupName(dto.getGroupName());
        user.setPassword(getEncPass(dto.getPassword()));
        user.setAccesses(new ArrayList<>() {{
            add(Access.GROUP);
        }});
        user.setStatus(AccountStatus.PENDING);
        if (user.getId() == null)
            user.setId(new ObjectId());

        Group g;
        if (group.isEmpty()) {
            int code = Utility.randIntForGroupCode();
            Optional<Group> tmp = groupRepository.findByCode(code);

            while (tmp.isPresent())
                code = Utility.randIntForGroupCode();

            g = Group.builder()
                    .name(dto.getGroupName())
                    .code(code)
                    .build();

            g.setOwner(user.getId());
            groupRepository.insert(g);
        } else {
            g = group.get();
            if (!g.getName().equals(dto.getGroupName())) {
                g.setName(dto.getGroupName());
                groupRepository.save(g);
            }
        }

        user.setGroupId(g.getId());
        user.setGroupName(g.getName());

        activationRepository.delete(activation);

        if (user.getId() == null)
            userRepository.insert(user);
        else
            userRepository.save(user);

        return new ResponseEntity<>(
                jwtTokenProvider.createToken(user.getNid(), user.getAccesses(), user.getGroupId(), user.getId()),
                HttpStatus.OK
        );
    }

    @CacheEvict(value = "user", key = "#userId")
    public ResponseEntity updateInfo(ObjectId userId, UpdatePersonalInfo dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidIdException::new);

        if (
                !user.getNid().equals(dto.getNid()) &&
                        userRepository.countByNID(dto.getNid()) > 0
        )
            throw new InvalidFieldsException("کد ملی وارد شده در سیستم موجود است");

        user.setName(dto.getName());
        user.setNid(dto.getNid());
        user.setCid(dto.getCid());
        user.setFatherName(dto.getFatherName());
        user.setBirthDay(dto.getBirthDay());
        user.setField(dto.getField());
        user.setUniversity(dto.getUniversity());
        user.setEndManageYear(dto.getEndManageYear());
        user.setUniversityYear(dto.getUniversityYear());
        user.setSex(dto.getSex());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @CacheEvict(value = "user", key = "#user.id")
    public void signUpStep2ForGroups(User user, SignUpStep2ForGroupData dto) {
        copyProperties(dto, user);
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#user.id")
    public void signUpStep3ForGroups(User user, SignUpStep3ForGroupData dto) {
        copyProperties(dto, user);
        userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#user.id")
    public void signUpStep4ForGroups(User user, SignUpStep4ForGroupData dto) {
        copyProperties(dto, user);
        userRepository.save(user);
    }

    public ResponseEntity<List<User>> findMembersDigestByRegionOwner(
            ObjectId userId, ObjectId groupId
    ) {
        List<Trip> trips =
                tripRepository.findActivesOrNotStartedProjectIdsByAreaOwnerId(Utility.getCurrLocalDateTime(), userId);

        if (trips.size() == 0)
            throw new NotAccessException();

        return new ResponseEntity<>(
                userCustomRepository.findAdvanced(
                        AccountStatus.ACTIVE, Access.JAHADI,
                        null, null, null, null,
                        null, groupId, null, null,
                        Pageable.ofSize(Integer.MAX_VALUE).withPage(0),
                        new String[] {"_id", "name", "NID", "phone", "university", "birth_day", "field"},
                        Criteria.where("_id").ne(userId),
                        Criteria.where("accesses").ne(Access.GROUP)
                ).getContent(),
                HttpStatus.OK
        );
    }

    @CacheEvict(value = "user", key = "#userId")
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

        Integer oldTripsCount = user.getOldTripsCount();
        int totalTripsCount = oldTripsCount == null
                ? tripService.getUserTripsCount(user.getRole(), Objects.equals(user.getRole(), Access.GROUP) ? user.getGroupId() : userId)
                : tripService.getUserTripsCount(user.getRole(), Objects.equals(user.getRole(), Access.GROUP) ? user.getGroupId() : userId) + oldTripsCount;

        user.setTripsCount(totalTripsCount);
        user.setLevel(getTripLevel(totalTripsCount));

        return new ResponseEntity<>(
                user,
                HttpStatus.OK
        );
    }

    @Data
    @Builder
    public static class Level {
        private int level;
        private String title;
        private String desc;
    }

    private Level getTripLevel(int tripsCount) {
        if(tripsCount == 0) {
            return Level
                    .builder()
                    .level(1)
                    .title("نو جهادگر")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 3) {
            return Level
                    .builder()
                    .level(2)
                    .title("جهادگر همراه")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 10) {
            return Level
                    .builder()
                    .level(3)
                    .title("جهادگر فعال")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 20) {
            return Level
                    .builder()
                    .level(4)
                    .title("جهادگر پیشرو")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 30) {
            return Level
                    .builder()
                    .level(5)
                    .title("جهادگر پرتلاش")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 40) {
            return Level
                    .builder()
                    .level(6)
                    .title("جهادگر برتر")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 50) {
            return Level
                    .builder()
                    .level(7)
                    .title("جهادگر ارشد")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 60) {
            return Level
                    .builder()
                    .level(8)
                    .title("جهادگر فاتح")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 70) {
            return Level
                    .builder()
                    .level(9)
                    .title("جهادگر پیشکسوت")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 80) {
            return Level
                    .builder()
                    .level(10)
                    .title("ستون جهاد")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 90) {
            return Level
                    .builder()
                    .level(11)
                    .title("الگوی جهاد")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }
        if(tripsCount <= 100) {
            return Level
                    .builder()
                    .level(12)
                    .title("اسطوره جهاد")
                    .desc("شما تاکنون در " + tripsCount + " اردو شرکت کرده اید.")
                    .build();
        }

        return null;
    }

    public ResponseEntity<List<UserDigest>> findGroupActiveMembersName(ObjectId groupId) {
        return ResponseEntity.ok(userRepository.findGroupActiveMembersName(groupId));
    }

    public ResponseEntity<MyAccesses> myAccesses(ObjectId userId, ObjectId groupId) {
        if(groupId == null) {
            return ResponseEntity.ok().body(MyAccesses.builder().build());
        }

        List<WareHouseAccessForGroup> wareHouseAccesses =
                wareHouseAccessService.getWareHouseAccessesByGroupId(groupId);

        return ResponseEntity
                .ok()
                .body(
                        MyAccesses
                                .builder()
                                .reportAccess(
                                        groupReportService
                                                .getGroupReporterUsers(groupId)
                                                .contains(userId)
                                )
                                .drugAccess(
                                        wareHouseAccesses
                                                .stream()
                                                .filter(WareHouseAccessForGroup::getHasAccessForDrug)
                                                .anyMatch(wareHouseAccessForGroup -> wareHouseAccessForGroup.getUserId().equals(userId))
                                )
                                .equipmentAccess(
                                        wareHouseAccesses
                                                .stream()
                                                .filter(WareHouseAccessForGroup::getHasAccessForEquipment)
                                                .anyMatch(wareHouseAccessForGroup -> wareHouseAccessForGroup.getUserId().equals(userId))
                                )
                                .externalReferralAccess(
                                        externalReferralAccessForGroupService
                                                .getGroupExternalReferralAccessesByGroupId(groupId)
                                                .contains(userId)
                                )
                                .build()
                );
    }

    public void setUserOldTripsCount(ObjectId groupId, ObjectId userId, Integer tripsCount) {
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if(!Objects.equals(user.getGroupId(), groupId))
            throw new NotAccessException();

        user.setOldTripsCount(tripsCount);
        userRepository.save(user);
    }
}
