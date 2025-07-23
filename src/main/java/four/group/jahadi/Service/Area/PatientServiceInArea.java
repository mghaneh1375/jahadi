package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.ModuleForms.PatientFormData;
import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
import four.group.jahadi.DTO.Patient.TrainFormData;
import four.group.jahadi.Enums.AgeType;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Enums.Module.QuestionType;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Area.*;
import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Service.ExcelService;
import four.group.jahadi.Utility.FileUtils;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findModule;
import static four.group.jahadi.Service.Area.AreaUtils.findStartedArea;
import static four.group.jahadi.Service.Area.ReportUtil.prepareHttpServletResponse;
import static four.group.jahadi.Utility.FileUtils.removeFile;
import static four.group.jahadi.Utility.FileUtils.uploadFile;
import static four.group.jahadi.Utility.StaticValues.ONE_MB;

@Service
public class PatientServiceInArea {

    public final static String UPLOAD_FOLDER = "patients_docs";

    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private ReportServiceInArea reportServiceInArea;

    public ResponseEntity<List<PatientJoinArea>> getPatients(ObjectId userId, ObjectId areaId) {
        //todo: check finalize
        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        findStartedArea(trip, areaId);
        List<PatientJoinArea> list = patientsInAreaRepository.findPatientsByAreaId(areaId);
        list.sort(Comparator.comparing(PatientJoinArea::getCreatedAt, Comparator.reverseOrder()));
        return new ResponseEntity<>(
                list,
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PatientJoinArea>> getInsuranceList(
            ObjectId userId, ObjectId areaId,
            Boolean justHasInsurance, Boolean justHasNotInsurance
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndInsurancerId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        findStartedArea(trip, areaId);
        List<PatientJoinArea> areaPatients = patientsInAreaRepository.findPatientsByAreaId(areaId);
        if ((justHasInsurance == null || !justHasInsurance) &&
                (justHasNotInsurance == null || !justHasNotInsurance))
            return new ResponseEntity<>(areaPatients, HttpStatus.OK);

        List<PatientJoinArea> output = new ArrayList<>();
        for (PatientJoinArea itr : areaPatients) {

            if (justHasInsurance != null && justHasInsurance && itr.getPatientInfo().getInsurance().equals(Insurance.NONE))
                continue;

            if (justHasNotInsurance != null && justHasNotInsurance && !itr.getPatientInfo().getInsurance().equals(Insurance.NONE))
                continue;

            output.add(itr);
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public ResponseEntity<List<PatientJoinArea>> getTrainList(
            ObjectId userId, ObjectId areaId,
            Boolean justAdult, Boolean justChildren,
            Boolean justTrained, Boolean justNotTrained
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        findStartedArea(trip, areaId);
        List<PatientJoinArea> patientsInArea = patientsInAreaRepository.findPatientsByAreaId(areaId);

        if ((justAdult == null || !justAdult) &&
                (justChildren == null || !justChildren) &&
                (justTrained == null || !justTrained) &&
                (justNotTrained == null || !justNotTrained)
        )
            return new ResponseEntity<>(patientsInArea, HttpStatus.OK);

        List<PatientJoinArea> output = new ArrayList<>();
        for (PatientJoinArea itr : patientsInArea) {

//            boolean isAdult = itr.getPatientInfo().getBirthDate() != null && Period.between(
//                    itr.getPatientInfo().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
//                    new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//            ).getYears() > 10;
            boolean isAdult = Objects.equals(itr.getPatientInfo().getAgeType(), AgeType.ADULT);

            if (justAdult != null && justAdult && !isAdult)
                continue;

            if (justChildren != null && justChildren && isAdult)
                continue;

            if (justTrained != null && justTrained && !itr.getTrained())
                continue;

            if (justNotTrained != null && justNotTrained && itr.getTrained())
                continue;

            output.add(itr);
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public void updatePatient(
            ObjectId patientId, ObjectId userId,
            ObjectId areaId, PatientData patientData
    ) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(InvalidIdException::new);
        if (!patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new NotAccessException();

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);
        findStartedArea(trip, areaId);

        patient.setAgeType(patientData.getAgeType());
        patient.setJob(patientData.getJob());
        patient.setSex(patientData.getSex());
        patient.setPhone(patientData.getPhone());
        patient.setName(patientData.getName());
        patient.setFatherName(patientData.getFatherName());
        patient.setIdentifier(patientData.getIdentifier());
        patient.setIdentifierType(patientData.getIdentifierType());
        patient.setInsurance(patientData.getInsurance());
        patient.setPatientNo(patientData.getPatientNo());
        patient.setBirthDate(Utility.getLocalDateTime(new Date(patientData.getBirthDate())));

        patientRepository.save(patient);
    }

    public void createPatientAndAddToRegion(ObjectId userId, ObjectId areaId, PatientData patientData) {

        //todo: check finalize

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area area = findStartedArea(trip, areaId);

        if (patientRepository.countByIdentifierAndIdentifierType(
                patientData.getIdentifier(), patientData.getIdentifierType()
        ) > 0)
            throw new InvalidFieldsException("اطلاعات بیمار پیش از این وارد شده است");

        patientsInAreaRepository.findPatientsIdentifierByAreaId(areaId)
                .stream().filter(patient -> patient.getIdentifier().equals(patientData.getIdentifier()))
                .findFirst().ifPresent(patient -> {
                    throw new InvalidFieldsException("کاربر مدنظر در منطقه موردنظر پذیرش شده است");
                });

        Patient newPatient = Patient.builder().ageType(patientData.getAgeType())
                .job(patientData.getJob())
                .sex(patientData.getSex())
                .phone(patientData.getPhone())
                .name(patientData.getName())
                .fatherName(patientData.getFatherName())
                .identifier(patientData.getIdentifier())
                .identifierType(patientData.getIdentifierType())
                .insurance(patientData.getInsurance())
                .patientNo(patientData.getPatientNo())
                .birthDate(Utility.getLocalDateTime(new Date(patientData.getBirthDate())))
                .build();

        patientRepository.insert(newPatient);
        doAddPatientToRegion(area, newPatient.getId(), newPatient.getAgeType());
    }

    private void doAddPatientToRegion(Area area, ObjectId patientId, AgeType ageType) {

        PatientsInArea patientInArea = PatientsInArea.builder()
                .patientId(patientId)
                .areaId(area.getId())
                .build();

        patientsInAreaRepository.insert(patientInArea);
    }

    public void addPatientToRegion(ObjectId userId, ObjectId areaId, ObjectId patientId) {

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area area = findStartedArea(trip, areaId);
        Patient patient = patientRepository.findById(patientId).orElseThrow(InvalidIdException::new);

        if (patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new InvalidFieldsException("فرد مورد نظر پیش از این افزوده شده است");

        doAddPatientToRegion(area, patientId, patient.getAgeType());
    }

    public ResponseEntity<Patient> inquiryPatient(
            ObjectId userId, ObjectId areaId,
            InquiryPatientData patientData,
            ObjectId groupId
    ) {
        if (groupId != null)
            tripRepository.findByGroupIdAndAreaId(groupId, areaId).orElseThrow(NotAccessException::new);
        else
            tripRepository.findActiveByAreaIdAndResponsibleId(areaId, userId, Utility.getCurrLocalDateTime())
                    .orElseThrow(NotAccessException::new);

        Patient patient = patientRepository.findByIdentifierAndIdentifierType(
                patientData.getIdentifier(), patientData.getIdentifierType()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کاربری یافت نشد");
        });

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    public List<PatientReferral> doAddReferral(
            List<PatientReferral> referrals,
            ObjectId moduleId,
            boolean addDuplicate,
            String desc,
            ObjectId referBy
    ) {

        if (referrals == null)
            referrals = new ArrayList<>();

        List<PatientReferral> wantedPatientReferral =
                referrals.stream().filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                        .collect(Collectors.toList());

        if (wantedPatientReferral.stream().anyMatch(patientReferral -> !patientReferral.isRecepted()) ||
                (!addDuplicate && wantedPatientReferral.size() > 0)
        )
            return referrals;

        referrals.add(
                (PatientReferral) PatientReferral
                        .builder()
                        .moduleId(moduleId)
                        .desc(desc)
                        .referBy(referBy)
                        .build().createId()
        );

        return referrals;
    }

    public void addReferralForPatientByOwner(
            ObjectId ownerId, ObjectId areaId,
            ObjectId patientId, ObjectId moduleId,
            String desc
    ) {
        Trip trip = tripRepository.findByAreaIdAndOwnerId(areaId, ownerId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        foundArea.getModules().stream().filter(module ->
                module.getModuleId().equals(moduleId)).findFirst().orElseThrow(InvalidIdException::new);

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        patientInArea.setReferrals(
                doAddReferral(
                        patientInArea.getReferrals(),
                        moduleId,
                        true,
                        desc, null
                )
        );
        patientsInAreaRepository.save(patientInArea);
    }

    public void addReferralForPatient(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, ObjectId moduleId,
            String desc
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        ModuleInArea moduleInArea = findModule(foundArea, moduleId, userId, null);
        Module module = moduleRepository.findById(moduleInArea.getModuleId()).orElseThrow(UnknownError::new);

        if (!module.isReferral())
            throw new InvalidFieldsException("در این ماژول امکان ارجاع دهی وجود ندارد");

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        patientInArea.setReferrals(
                doAddReferral(
                        patientInArea.getReferrals(),
                        moduleId,
                        true,
                        desc, userId
                )
        );
        patientsInAreaRepository.save(patientInArea);
    }


    public void addReferralForPatientBySubModule(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, ObjectId srcModuleId,
            ObjectId srcSubModuleId
    ) {

        Module module = moduleRepository.findById(srcModuleId).orElseThrow(InvalidIdException::new);
        SubModule subModule = module.getSubModules()
                .stream()
                .filter(subModuleItr -> subModuleItr.getId().equals(srcSubModuleId))
                .findFirst()
                .orElseThrow(InvalidIdException::new);

        if (!subModule.isReferral())
            throw new InvalidFieldsException("در این بخش امکان ارجاع دهی وجود ندارد");

        Trip trip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        findModule(foundArea, srcModuleId, foundArea.getOwnerId().equals(userId) ? null : userId, null);

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        patientInArea
                .getReferrals()
                .stream()
                .filter(patientReferral -> patientReferral.getModuleId().equals(srcModuleId))
                .findFirst().orElseThrow(NotAccessException::new);

        Optional<ModuleInArea> tmp = foundArea
                .getModules()
                .stream()
                .filter(m -> m.getModuleId().equals(subModule.getReferTo()))
                .findFirst();

        if (tmp.isEmpty())
            return;

        patientInArea.setReferrals(
                doAddReferral(
                        patientInArea.getReferrals(),
                        subModule.getReferTo(),
                        true,
                        null, userId
                )
        );
        patientsInAreaRepository.save(patientInArea);
    }

    public void setPatientTrainFrom(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, TrainFormData data
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        if (!foundArea.getOwnerId().equals(userId) &&
                (
                        foundArea.getTrainers() == null ||
                                !foundArea.getTrainers().contains(userId)
                )
        )
            throw new NotAccessException();

        PatientsInArea patient = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        patient.setTrainForm(
                TrainForm
                        .builder()
                        .shepesh(data.getShepesh())
                        .weight(data.getWeight())
                        .height(data.getHeight())
                        .description(data.getDescription())
                        .recvShampoo(data.getRecvShampoo())
                        .recvCulturePackage(data.getRecvCulturePackage())
                        .BMI(data.getWeight() / Math.pow(data.getHeight() / 100.0, 2))
                        .build()
        );
        patientsInAreaRepository.save(patient);
    }

    public ResponseEntity<TrainForm> getPatientTrainFrom(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, ObjectId groupId
    ) {
        Trip trip;
        if (groupId != null)
            trip = tripRepository.findByGroupIdAndAreaId(groupId, areaId)
                    .orElseThrow(NotAccessException::new);
        else
            trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrLocalDateTime())
                    .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
//        if (!foundArea.getOwnerId().equals(userId) &&
//                (
//                        foundArea.getTrainers() == null ||
//                                !foundArea.getTrainers().contains(userId)
//                )
//        )
//            throw new NotAccessException();

        PatientsInArea patient = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        return new ResponseEntity<>(patient.getTrainForm(), HttpStatus.OK);
    }


    public ResponseEntity<List<PatientReferral>> getPatientReferrals(ObjectId userId, ObjectId areaId, ObjectId patientId) {

        Trip trip = tripRepository.findActiveByAreaIdAndResponsibleId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        PatientsInArea patient = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        List<PatientReferral> referrals = patient.getReferrals();
        if (referrals == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        List<ModuleInArea> modules = foundArea.getModules();
        HashMap<ObjectId, PairValue> moduleNames = new HashMap<>();

        referrals.forEach(patientReferral -> {
            patientReferral.setForms(null);
            if (moduleNames.containsKey(patientReferral.getModuleId())) {
                patientReferral.setModuleName(moduleNames.get(patientReferral.getModuleId()).getKey().toString());
                patientReferral.setModuleTabName(moduleNames.get(patientReferral.getModuleId()).getValue().toString());
            } else {
                modules.stream().filter(module -> module.getModuleId().equals(patientReferral.getModuleId()))
                        .findFirst().ifPresent(module -> {
                            patientReferral.setModuleName(module.getModuleName());
                            moduleNames.put(module.getModuleId(), new PairValue(module.getModuleName(), module.getModuleTabName()));
                        });
            }
        });

        return new ResponseEntity<>(referrals, HttpStatus.OK);
    }

    public void setPatientTrainStatus(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, Boolean hasTrained
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        if (!foundArea.getOwnerId().equals(userId) &&
                (
                        foundArea.getTrainers() == null ||
                                !foundArea.getTrainers().contains(userId)
                )
        )
            throw new NotAccessException();

        PatientsInArea patient = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);
        patient.setTrained(hasTrained);

        if (hasTrained) {
            foundArea.getModules().stream()
                    .filter(module ->
                            module.getModuleName().equals("غربالگری پایه") ||
                                    module.getModuleName().equals("غربالگری روان")
                    )
                    .forEach(module -> patient.setReferrals(
                            doAddReferral(
                                    patient.getReferrals(),
                                    module.getModuleId(),
                                    false,
                                    null, null
                            )
                    ));
        }

        patientsInAreaRepository.save(patient);
    }

    public void setPatientInsuranceStatus(
            ObjectId userId, ObjectId areaId, ObjectId patientId, Insurance insuranceStatus
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndInsurancerId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area foundArea = findStartedArea(trip, areaId);
        if (!foundArea.getOwnerId().equals(userId) &&
                (
                        foundArea.getInsurancers() == null ||
                                !foundArea.getInsurancers().contains(userId)
                )
        )
            throw new NotAccessException();

        if (!patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new InvalidIdException();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        patient.setInsurance(insuranceStatus);
        patientRepository.save(patient);
    }

    public ResponseEntity<HashMap<String, Object>> getModulePatients(
            ObjectId userId, ObjectId areaId, ObjectId moduleId,
            Boolean justRecepted, Boolean justUnRecepted
    ) {

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        ModuleInArea moduleInArea = AreaUtils.findModule(
                area, moduleId,
                null, null
//              todo:
//                userId.equals(area.getOwnerId()) ? null : userId,
//                userId.equals(area.getOwnerId()) ? null : userId
        );

        List<PatientJoinArea> patients = patientsInAreaRepository.findPatientsListInModuleByAreaId(
                areaId, moduleId
        );

        boolean noFilter = (justRecepted == null || !justRecepted) &&
                (justUnRecepted == null || !justUnRecepted);

        List<PatientJoinArea> output = new ArrayList<>();
        for (PatientJoinArea patientJoinArea : patients) {
            if (noFilter || patientJoinArea.getReferrals().stream()
                    .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                    .reduce((first, second) -> second)
                    .filter(patientReferral ->
                            Boolean.TRUE.equals(justRecepted) == patientReferral.isRecepted()
                    ).isPresent()) {
                List<PatientReferral> referrals = patientJoinArea.getReferrals().stream()
                        .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId)).collect(Collectors.toList());
                patientJoinArea.setReferrals(referrals);
                output.add(patientJoinArea);
            }
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("moduleName", moduleInArea.getModuleName());
        hashMap.put("patients", output);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public void setReceptionStatusOfPatient(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId patientId, boolean isRecepted
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        AreaUtils.findModule(
                area, moduleId,
                userId.equals(area.getOwnerId()) ? null : userId,
                userId.equals(area.getOwnerId()) ? null : userId
        );

        PatientsInArea patientInArea =
                patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId).orElseThrow(InvalidIdException::new);

        if (patientInArea.getReferrals() == null)
            throw new InvalidIdException();

        Optional<PatientReferral> optionalPatientReferral =
                patientInArea.getReferrals().stream()
                        .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                        .reduce((first, second) -> second);

        if (optionalPatientReferral.isEmpty())
            throw new InvalidIdException();

        PatientReferral wantedReferral = optionalPatientReferral.get();
        wantedReferral.setRecepted(isRecepted);
        if (isRecepted)
            wantedReferral.setReceptedAt(LocalDateTime.now());

        patientsInAreaRepository.save(patientInArea);
    }

    private List<ObjectId> getMandatoryQuestionIds(List<Question> questions) {

        List<ObjectId> mandatoryQuestionIds = questions.stream()
                .filter(question ->
                        (question.getQuestionType().equals(QuestionType.SIMPLE) && ((SimpleQuestion) question).getRequired()) ||
                                (question.getQuestionType().equals(QuestionType.TABLE) && ((TableQuestion) question).getRequired())
                )
                .map(Question::getId).collect(Collectors.toList());

        mandatoryQuestionIds.addAll(
                questions.stream()
                        .filter(question -> question.getQuestionType().equals(QuestionType.CHECK_LIST) && question instanceof SimpleQuestion && ((SimpleQuestion) question).getRequired())
                        .map(question -> question.getId())
                        .collect(Collectors.toList())
        );

        mandatoryQuestionIds.addAll(
                questions.stream()
                        .filter(question -> question.getQuestionType().equals(QuestionType.CHECK_LIST) && question instanceof CheckListGroupQuestion)
                        .map(question -> ((CheckListGroupQuestion) question).getQuestions())
                        .flatMap(List::stream)
                        .filter(SimpleQuestion::getRequired)
                        .map(Question::getId)
                        .collect(Collectors.toList())
        );

        mandatoryQuestionIds.addAll(
                questions.stream()
                        .filter(question -> question.getQuestionType().equals(QuestionType.GROUP))
                        .map(question -> ((GroupQuestion) question).getQuestions())
                        .flatMap(List::stream)
                        .filter(question ->
                                (question.getQuestionType().equals(QuestionType.SIMPLE) && ((SimpleQuestion) question).getRequired()) ||
                                        (question.getQuestionType().equals(QuestionType.TABLE) && ((TableQuestion) question).getRequired())
                        )
                        .map(Question::getId)
                        .collect(Collectors.toList())
        );

        return mandatoryQuestionIds;
    }

    private HashMap<ObjectId, A> getSubModuleAllQuestions(List<Question> questions) {

        HashMap<ObjectId, A> allQuestions = new HashMap<>();

        questions.stream()
                .filter(question ->
                        question.getQuestionType().equals(QuestionType.SIMPLE))
                .forEach(question -> {
                    SimpleQuestion simpleQuestion = ((SimpleQuestion) question);
                    allQuestions.put(
                            question.getId(),
                            A.builder()
                                    .questionType(QuestionType.SIMPLE)
                                    .canWriteDesc(simpleQuestion.getCanWriteDesc())
                                    .required(simpleQuestion.getRequired())
                                    .answerType(simpleQuestion.getAnswerType())
                                    .options(
                                            simpleQuestion.getOptions() == null ? null :
                                                    simpleQuestion.getOptions().stream().map(PairValue::getKey).collect(Collectors.toList())
                                    )
                                    .build()
                    );
                });

        questions.stream()
                .filter(question ->
                        question.getQuestionType().equals(QuestionType.TABLE))
                .forEach(question -> {
                    TableQuestion tableQuestion = (TableQuestion) question;
                    allQuestions.put(
                            question.getId(),
                            A.builder()
                                    .required(tableQuestion.getRequired())
                                    .questionType(QuestionType.TABLE)
                                    .rows(tableQuestion.getRowsCount())
                                    .cols(
                                            tableQuestion.getFirstColumn() != null && tableQuestion.getFirstColumn().size() > 0
                                                    ? tableQuestion.getHeaders().size() - 1
                                                    : tableQuestion.getHeaders().size()
                                    )
                                    .answerType(tableQuestion.getAnswerType())
                                    .build()
                    );
                });

        questions.stream()
                .filter(question -> question.getQuestionType().equals(QuestionType.CHECK_LIST))
                .forEach(checkListQuestion -> {
                    if (checkListQuestion instanceof CheckListGroupQuestion) {
                        CheckListGroupQuestion checkListGroupQuestion = ((CheckListGroupQuestion) checkListQuestion);
                        List<Object> options = checkListGroupQuestion.getOptions().stream().map(PairValue::getKey).map(Object::toString).collect(Collectors.toList());

                        checkListGroupQuestion.getQuestions()
                                .forEach(question -> {
                                    A a = A.builder()
                                            .required(question.getRequired())
                                            .questionType(QuestionType.CHECK_LIST)
                                            .answerType(AnswerType.TICK)
                                            .canWriteDesc(checkListGroupQuestion.getCanWriteDesc())
                                            .canWriteReason(checkListGroupQuestion.getCanWriteReason())
                                            .canWriteReport(checkListGroupQuestion.getCanWriteReport())
                                            .canWriteTime(checkListGroupQuestion.getCanWriteTime())
                                            .canWriteSampleInfoDesc(checkListGroupQuestion.getCanWriteSampleInfoDesc())
                                            .canUploadFile(checkListGroupQuestion.getCanUploadFile())
                                            .options(options)
                                            .build();

                                    if (checkListGroupQuestion.getMarkable()) {
                                        a.setParentId(checkListGroupQuestion.getId());
                                        a.setQuestionId(question.getId());
                                        a.setMarks(checkListGroupQuestion.getMarks());
                                    }

                                    allQuestions.put(question.getId(), a);
                                });
                    } else if (checkListQuestion instanceof SimpleQuestion) {
                        SimpleQuestion question = ((SimpleQuestion) checkListQuestion);
                        List<Object> options = question.getOptions().stream().map(PairValue::getKey).map(Object::toString).collect(Collectors.toList());

                        A a = A.builder()
                                .required(question.getRequired())
                                .questionType(QuestionType.CHECK_LIST)
                                .answerType(AnswerType.TICK)
                                .canWriteDesc(question.getCanWriteDesc())
                                .canWriteReason(false)
                                .canWriteReport(false)
                                .canWriteTime(false)
                                .canWriteSampleInfoDesc(false)
                                .canUploadFile(false)
                                .options(options)
                                .build();
                        allQuestions.put(question.getId(), a);
                    }
                });

        List<Question> groupQuestions = questions.stream()
                .filter(question -> question.getQuestionType().equals(QuestionType.GROUP))
                .map(question -> ((GroupQuestion) question).getQuestions())
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (groupQuestions.size() > 0) {
            HashMap<ObjectId, A> tmp = getSubModuleAllQuestions(groupQuestions);
            for (ObjectId id : tmp.keySet())
                allQuestions.put(id, tmp.get(id));
        }

        return allQuestions;
    }

    public void setPatientForm(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId subModuleId,
            ObjectId patientId, List<PatientFormData> formData,
            MultipartFile[] files
    ) {

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.getSize() > ONE_MB * 5)
                    throw new RuntimeException("حداکثر حجم مجاز 5MB می باشد");
                try {
                    String fileType = (String) FileUtils.getFileType(Objects.requireNonNull(file.getOriginalFilename())).getKey();
                    if (!fileType.equalsIgnoreCase("pdf") &&
                            !fileType.equalsIgnoreCase("image")
                    )
                        throw new RuntimeException("شما تنها مجاز به آپلود فایل های تصویری و PDF می باشد");
                } catch (Exception x) {
                    throw new RuntimeException(x.getMessage());
                }
            }
        }

        Module module = moduleRepository.findById(moduleId).orElseThrow(InvalidIdException::new);
        SubModule wantedSubModule = module.getSubModules().stream().filter(subModule -> subModule.getId().equals(subModuleId)).findFirst().orElseThrow(InvalidIdException::new);

        List<ObjectId> mandatoryQuestionIds = getMandatoryQuestionIds(wantedSubModule.getQuestions());
        HashMap<ObjectId, A> allQuestions = getSubModuleAllQuestions(wantedSubModule.getQuestions());

        if (formData.stream()
                .map(PatientFormData::getQuestionId)
                .anyMatch(objectId -> !allQuestions.containsKey(objectId))
        )
            throw new RuntimeException("آی دی سوالات حاوی مقداری است که نامعتبر می باشد");

        if (mandatoryQuestionIds.stream().anyMatch(questionId -> {
            A a = allQuestions.get(questionId);
            Optional<PatientFormData> tmp = formData.stream()
                    .filter(data -> data.getQuestionId().equals(questionId) &&
                            (
                                    (!a.getAnswerType().equals(AnswerType.UPLOAD) &&
                                            data.getAnswer() != null &&
                                            !data.getAnswer().toString().isEmpty()
                                    ) || (a.getAnswerType().equals(AnswerType.UPLOAD) &&
                                            data.getFileIndex() != null && data.getFileIndex() >= 0)
                            )
                    ).findFirst();
            return tmp.isEmpty();
        }))
            throw new RuntimeException("لطفا به تمامی سوالات اجباری پاسخ دهید");

        List<PatientAnswer> patientAnswers = new ArrayList<>();
        HashMap<ObjectId, Integer> marks = null;
        List<Integer> seenFileIndices = new ArrayList<>();

        for (PatientFormData data : formData) {
            A a = allQuestions.get(data.getQuestionId());

            if (data.getFileIndex() != null) {
                if (!a.getCanUploadFile() && !a.getAnswerType().equals(AnswerType.UPLOAD))
                    throw new RuntimeException("برای سوال " + data.getQuestionId() + " نمی توان فایلی آپلود کرد");

                if (files == null || files.length <= data.getFileIndex() || data.getFileIndex() < 0)
                    throw new RuntimeException("فایل سوال " + data.getQuestionId() + " آپلود نشده است");

                if (seenFileIndices.contains(data.getFileIndex()))
                    throw new RuntimeException("فایل شماره " + data.getFileIndex() + " دوبار استفاده شده است");

                seenFileIndices.add(data.getFileIndex());
            }

            if (data.getAnswer() == null || data.getAnswer().toString().isEmpty())
                continue;

            if (a.getQuestionType().equals(QuestionType.TABLE)) {

                String[] splited = data.getAnswer().toString().split("__");

                if (splited.length != a.getCols() * a.getRows())
                    throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " معتبر نیست");

                for (String split : splited) {

                    if (a.getAnswerType().equals(AnswerType.NUMBER) &&
                            !Utility.isNumeric(split)
                    )
                        throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید عدد باشد");

                    if (a.getAnswerType().equals(AnswerType.DOUBLE)) {
                        try {
                            Double.parseDouble(split);
                        } catch (Exception x) {
                            throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید عدد اعشاری باشد");
                        }
                    }
                }

            } else {
                switch (a.getAnswerType()) {
                    case NUMBER:
                        if (!(data.getAnswer() instanceof Number) && !Utility.isNumeric(data.getAnswer().toString()))
                            throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید عدد باشد");
                        break;
                    case TICK:
                        List<String> options = a.getOptions()
                                .stream().map(Object::toString).collect(Collectors.toList());

                        if (!options.contains(data.getAnswer().toString()))
                            throw new RuntimeException("گزینه انتخاب شده برای سوال " + data.getQuestionId().toString() + " معتبر نمی باشد");

                        if (a.getQuestionType().equals(QuestionType.CHECK_LIST) &&
                                a.getMarks() != null && a.getMarks().containsKey(data.getAnswer().toString())
                        ) {

                            if (marks == null)
                                marks = new HashMap<>();

                            if (marks.containsKey(a.getParentId()))
                                marks.put(a.getParentId(), marks.get(a.getParentId()) + a.getMarks().get(data.getAnswer().toString()));
                            else
                                marks.put(a.getParentId(), a.getMarks().get(data.getAnswer().toString()));
                        }
                        break;
                    case TEXT:
                        if (data.getAnswer().toString().length() > 100)
                            throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید حداکثر 100 کاراکتر باشد");
                        break;
                    case LONG_TEXT:
                        if (data.getAnswer().toString().length() > 1000)
                            throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید حداکثر 1000 کاراکتر باشد");
                        break;
                }
            }
        }

        List<String> filenames = new ArrayList<>();
        for (PatientFormData data : formData) {
            if (data.getFileIndex() != null) {
                String filename = uploadFile(files[data.getFileIndex()], UPLOAD_FOLDER);
                if (filename == null) {
                    for (String file : filenames)
                        removeFile(file, UPLOAD_FOLDER);
                    throw new RuntimeException("خطای ناشناخته هنگام بارگداری فایل");
                }
                filenames.add(filename);
            }
        }

        for (PatientFormData data : formData) {

            A a = allQuestions.get(data.getQuestionId());

            if (
                    (
                            !a.getAnswerType().equals(AnswerType.UPLOAD) && (
                                    data.getAnswer() == null || data.getAnswer().toString().isEmpty()
                            )
                    ) ||
                            (
                                    a.getAnswerType().equals(AnswerType.UPLOAD) && data.getFileIndex() == null
                            )
            )
                continue;

            PatientAnswer patientAnswer = PatientAnswer
                    .builder()
                    .questionId(data.getQuestionId())
                    .build();

            if (a.getAnswerType().equals(AnswerType.UPLOAD))
                patientAnswer.setUploadedFile(filenames.get(data.getFileIndex()));
            else
                patientAnswer.setAnswer(data.getAnswer());

            if (a.getCanWriteDesc() && data.getDesc() != null &&
                    !data.getDesc().isEmpty())
                patientAnswer.setDesc(data.getDesc());

            if (a.getCanWriteReason() && data.getReason() != null &&
                    !data.getReason().isEmpty())
                patientAnswer.setReason(data.getReason());

            if (a.getCanWriteReport() && data.getReport() != null &&
                    !data.getReport().isEmpty())
                patientAnswer.setReport(data.getReport());

            if (a.getCanWriteTime() && data.getTime() != null &&
                    !data.getTime().isEmpty())
                patientAnswer.setTime(data.getTime());

            if (a.getCanWriteSampleInfoDesc() && data.getSampleInfoDesc() != null &&
                    !data.getSampleInfoDesc().isEmpty())
                patientAnswer.setSampleInfoDesc(data.getSampleInfoDesc());

            if (a.getCanUploadFile() && data.getFileIndex() != null &&
                    filenames.size() > data.getFileIndex()
            )
                patientAnswer.setAdditionalUploadedFile(filenames.get(data.getFileIndex()));

            patientAnswers.add(patientAnswer);
        }

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        AreaUtils.findModule(
                area, moduleId,
                userId.equals(area.getOwnerId()) ? null : userId
        );

        PatientsInArea patientInArea =
                patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId).orElseThrow(InvalidIdException::new);

        Optional<PatientReferral> optionalPatientReferral =
                patientInArea.getReferrals().stream()
                        .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                        .reduce((first, second) -> second);

        if (optionalPatientReferral.isEmpty())
            throw new RuntimeException("بیمار مدنظر در ماژول مدنظر پذیرش نشده است");

        PatientReferral wantedReferral = optionalPatientReferral.get();
        PatientForm newPatientForm = (PatientForm) PatientForm
                .builder()
                .subModuleId(subModuleId)
                .answers(patientAnswers)
                .doctorId(userId)
                .build().createId();

        if (marks != null)
            newPatientForm.setMark(marks);

        List<PatientForm> patientForms = wantedReferral.getForms();
        if (patientForms == null)
            patientForms = new ArrayList<>();

        Optional<PatientForm> existPatientForm =
                patientForms.stream().filter(patientForm -> patientForm.getSubModuleId().equals(subModuleId)).findFirst();

        if (existPatientForm.isPresent()) {
            existPatientForm.get().setAnswers(patientAnswers);
            if (marks != null)
                existPatientForm.get().setMark(marks);
        } else
            patientForms.add(newPatientForm);

        wantedReferral.setForms(patientForms);
        patientsInAreaRepository.save(patientInArea);
    }

    public ResponseEntity<List<PatientAnswer>> getPatientForm(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId subModuleId,
            ObjectId patientId
    ) {

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(
                areaId, userId, moduleId
        ).orElseThrow(NotAccessException::new);

        Area area = AreaUtils.findStartedArea(trip, areaId);
        AreaUtils.findModule(
                area, moduleId,
                null
//              todo: userId.equals(area.getOwnerId()) ? null : userId
        );

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(
                areaId, patientId
        ).orElseThrow(InvalidIdException::new);

        Module wantedModule = moduleRepository.findById(moduleId).orElseThrow(RuntimeException::new);
        SubModule wantedSubModule = wantedModule.getSubModules().stream().filter(subModule -> subModule.getId().equals(subModuleId))
                .findFirst().orElseThrow(RuntimeException::new);

        ObjectId wantedModuleId = moduleId;
        ObjectId wantedSubModuleId = subModuleId;

        if (wantedSubModule.getReadOnlyModuleId() != null &&
                wantedSubModule.getReadOnlySubModuleId() != null
        ) {
            wantedModuleId = wantedSubModule.getReadOnlyModuleId();
            wantedSubModuleId = wantedSubModule.getReadOnlySubModuleId();
        }

        ObjectId finalWantedModuleId = wantedModuleId;
        Optional<PatientReferral> optionalPatientReferral =
                patientInArea.getReferrals().stream()
                        .filter(patientReferral -> patientReferral.getModuleId().equals(finalWantedModuleId))
                        .reduce((first, second) -> second);

        if (optionalPatientReferral.isEmpty() ||
                optionalPatientReferral.get().getForms() == null
        )
            throw new RuntimeException("فرمی برای این ماژول ثبت نشده است");

        ObjectId finalWantedSubModuleId = wantedSubModuleId;
        PatientForm wantedPatientForm =
                optionalPatientReferral.get()
                        .getForms().stream()
                        .filter(patientForm -> patientForm.getSubModuleId().equals(finalWantedSubModuleId))
                        .reduce((first, second) -> second)
                        .stream().findFirst()
                        .orElseThrow(() -> {
                            throw new RuntimeException("فرمی برای این ماژول ثبت نشده است");
                        });

        List<PatientAnswer> answers = wantedPatientForm.getAnswers();

        if (wantedPatientForm.getMark() != null)
            for (ObjectId key : wantedPatientForm.getMark().keySet())
                answers.add(
                        PatientAnswer
                                .builder()
                                .questionId(key)
                                .answer("__mark__" + wantedPatientForm.getMark().get(key))
                                .build()
                );

        return new ResponseEntity<>(
                answers,
                HttpStatus.OK
        );
    }

    public void removePatientFromArea(ObjectId userId, ObjectId areaId, ObjectId patientId) {
        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrLocalDateTime())
                .orElseThrow(NotAccessException::new);

        Area area = findStartedArea(trip, areaId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        patientsInAreaRepository.deleteByAreaIdAndPatientId(areaId, patientId);
    }

    public void patientReport(
            ObjectId patientId, ObjectId areaId, Boolean justCurrArea,
            ObjectId wantedModuleId, HttpServletResponse response
    ) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(InvalidIdException::new);
        List<PatientsInArea> patientForms = null;
        if (justCurrArea) {
            Optional<PatientsInArea> form = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId);
            if (form.isPresent())
                patientForms = Collections.singletonList(form.get());
        } else patientForms = patientsInAreaRepository.findByPatientId(patientId);

        HashMap<ObjectId, HashMap<ObjectId, List<PatientForm>>> forms = new HashMap<>();
        if (patientForms == null) {
            return;
        }
        patientForms.forEach(patientForm -> {
            patientForm.getReferrals().forEach(patientReferral -> {
                if (!forms.containsKey(patientReferral.getModuleId())) {
                    forms.put(
                            patientReferral.getModuleId(),
                            new HashMap<>(){{
                                put(
                                        patientReferral.getId(),
                                        patientReferral.getForms()
                                );
                            }}
                    );
                }
                else {
                    forms.get(patientReferral.getModuleId()).put(patientReferral.getId(), patientReferral.getForms());
                }
            });
        });

        List<Module> modules = moduleRepository.findAllById(new ArrayList<>(forms.keySet()));
        List<String> modulesName = modules.stream()
                .map(module -> module.getName().replace("/", " "))
                .collect(Collectors.toList());

        Workbook workbook = excelService.createExcel(modulesName);
        AtomicBoolean a = new AtomicBoolean(false);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            reportServiceInArea.getPatientReport(
                    patient, modules.get(i), workbook.getSheetAt(i), forms.get(modules.get(i).getId())
            );
        }
        prepareHttpServletResponse(response, workbook, "patientReport");
    }
}
