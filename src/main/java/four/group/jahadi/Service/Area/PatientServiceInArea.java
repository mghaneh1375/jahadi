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
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.Area.AreaUtils.findModule;
import static four.group.jahadi.Service.Area.AreaUtils.findStartedArea;

@Service
public class PatientServiceInArea {

    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    TripRepository tripRepository;

    public ResponseEntity<List<PatientJoinArea>> getPatients(ObjectId userId, ObjectId areaId) {

        //todo: check finalize

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        findStartedArea(trip, areaId);

        return new ResponseEntity<>(
                patientsInAreaRepository.findPatientsByAreaId(areaId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PatientJoinArea>> getInsuranceList(
            ObjectId userId, ObjectId areaId,
            Boolean justHasInsurance, Boolean justHasNotInsurance
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndInsurancerId(areaId, userId, Utility.getCurrDate())
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

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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

    public void createPatientAndAddToRegion(ObjectId userId, ObjectId areaId, PatientData patientData) {

        //todo: check finalize

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
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
                .birthDate(new Date(patientData.getBirthDate()))
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

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        Area area = findStartedArea(trip, areaId);
        Patient patient = patientRepository.findById(patientId).orElseThrow(InvalidIdException::new);

        if (patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new InvalidFieldsException("فرد مورد نظر پیش از این افزوده شده است");

        doAddPatientToRegion(area, patientId, patient.getAgeType());
    }

    public ResponseEntity<Patient> inquiryPatient(ObjectId userId, ObjectId areaId,
                                                  InquiryPatientData patientData) {

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        Patient patient = patientRepository.findByIdentifierAndIdentifierType(
                patientData.getIdentifier(), patientData.getIdentifierType()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کاربری یافت نشد");
        });

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    private List<PatientReferral> doAddReferral(
            List<PatientReferral> referrals,
            ObjectId moduleId,
            boolean addDuplicate,
            String desc
    ) {

        if (referrals == null)
            referrals = new ArrayList<>();

        if (!addDuplicate) {
            Optional<PatientReferral> wantedPatientReferral =
                    referrals.stream().filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                            .findFirst();
            if (wantedPatientReferral.isPresent())
                return referrals;
        }

        referrals.add(
                (PatientReferral) PatientReferral
                        .builder()
                        .moduleId(moduleId)
                        .desc(desc)
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
                        desc
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
                        desc
                )
        );
        patientsInAreaRepository.save(patientInArea);
    }

    public void setPatientTrainFrom(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, TrainFormData data
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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
                        .BMI(data.getWeight() / Math.pow(data.getHeight(), 2))
                        .build()
        );
        patientsInAreaRepository.save(patient);
    }

    public ResponseEntity<TrainForm> getPatientTrainFrom(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId
    ) {

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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

        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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

        List<PatientReferral> referrals = patient.getReferrals();
        if (referrals == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        List<ModuleInArea> modules = foundArea.getModules();
        HashMap<ObjectId, String> moduleNames = new HashMap<>();

        referrals.forEach(patientReferral -> {
            patientReferral.setForms(null);
            if (moduleNames.containsKey(patientReferral.getModuleId()))
                patientReferral.setModuleName(moduleNames.get(patientReferral.getModuleId()));
            else {
                modules.stream().filter(module -> module.getModuleId().equals(patientReferral.getModuleId()))
                        .findFirst().ifPresent(module -> {
                            patientReferral.setModuleName(module.getModuleName());
                            moduleNames.put(module.getModuleId(), module.getModuleName());
                        });
            }
        });

        return new ResponseEntity<>(referrals, HttpStatus.OK);
    }

    public void setPatientTrainStatus(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId, Boolean hasTrained
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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
                    .filter(module -> module.getModuleName().contains("غربالگری"))
                    .findFirst().ifPresent(module -> patient.setReferrals(
                            doAddReferral(
                                    patient.getReferrals(),
                                    module.getModuleId(),
                                    false,
                                    null
                            )
                    ));
        }

        patientsInAreaRepository.save(patient);
    }

    public void setPatientInsuranceStatus(
            ObjectId userId, ObjectId areaId, ObjectId patientId, Insurance insuranceStatus
    ) {
        Trip trip = tripRepository.findActiveByAreaIdAndTrainerId(areaId, userId, Utility.getCurrDate())
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

    public ResponseEntity<List<PatientJoinArea>> getModulePatients(
            ObjectId userId, ObjectId areaId, ObjectId moduleId,
            Boolean justRecepted, Boolean justUnRecepted
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

        return new ResponseEntity<>(output, HttpStatus.OK);
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
            wantedReferral.setReceptedAt(new Date());

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
                        .filter(question -> question.getQuestionType().equals(QuestionType.CHECK_LIST))
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
                                    .cols(tableQuestion.getHeaders().size())
                                    .answerType(tableQuestion.getAnswerType())
                                    .build()
                    );
                });

        questions.stream()
                .filter(question -> question.getQuestionType().equals(QuestionType.CHECK_LIST))
                .forEach(checkListQuestion -> {
                    CheckListGroupQuestion checkListGroupQuestion = ((CheckListGroupQuestion) checkListQuestion);
                    List<Object> options = checkListGroupQuestion.getOptions().stream().map(PairValue::getKey).map(Object::toString).collect(Collectors.toList());

                    checkListGroupQuestion.getQuestions()
                            .forEach(question -> {

                                A a = A.builder()
                                        .required(question.getRequired())
                                        .questionType(QuestionType.CHECK_LIST)
                                        .answerType(AnswerType.TICK)
                                        .options(options)
                                        .build();

                                if (checkListGroupQuestion.getMarkable()) {
                                    a.setParentId(checkListGroupQuestion.getId());
                                    a.setQuestionId(question.getId());
                                    a.setMarks(checkListGroupQuestion.getMarks());
                                }

                                allQuestions.put(question.getId(), a);
                            });
                });

        List<Question> groupQuestions = questions.stream()
                .filter(question -> question.getQuestionType().equals(QuestionType.GROUP))
                .map(question -> ((GroupQuestion) question).getQuestions())
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if(groupQuestions.size() > 0) {
            HashMap<ObjectId, A> tmp = getSubModuleAllQuestions(groupQuestions);
            for (ObjectId id : tmp.keySet())
                allQuestions.put(id, tmp.get(id));
        }

        return allQuestions;
    }

    public void setPatientForm(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId subModuleId,
            ObjectId patientId, List<PatientFormData> formData
    ) {

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
            Optional<PatientFormData> tmp = formData.stream()
                    .filter(data -> data.getAnswer() != null &&
                            !data.getAnswer().toString().isEmpty() &&
                            data.getQuestionId().equals(questionId)
                    ).findFirst();
            return tmp.isEmpty();
        }))
            throw new RuntimeException("لطفا به تمامی سوالات اجباری پاسخ دهید");

        List<PatientAnswer> patientAnswers = new ArrayList<>();
        HashMap<ObjectId, Integer> marks = null;

        for (PatientFormData data : formData) {

            if (data.getAnswer() == null || data.getAnswer().toString().isEmpty())
                continue;

            A a = allQuestions.get(data.getQuestionId());
            if(a.getQuestionType().equals(QuestionType.TABLE)) {

                String[] splited = data.getAnswer().toString().split("___");

                if(splited.length != a.getCols() * a.getRows())
                    throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " معتبر نیست");

                for(String split : splited) {

                    if (a.getAnswerType().equals(AnswerType.NUMBER) &&
                            !Utility.isNumeric(split)
                    )
                        throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید عدد باشد");

                    if (a.getAnswerType().equals(AnswerType.DOUBLE)) {
                        try {
                            Double.parseDouble(split);
                        }
                        catch (Exception x) {
                            throw new RuntimeException("پاسخ به سوال " + data.getQuestionId().toString() + " باید عدد اعشاری باشد");
                        }
                    }
                }

            }
            else {
                switch (a.getAnswerType()) {
                    case NUMBER:
                        if (!(data.getAnswer() instanceof Number))
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

            PatientAnswer patientAnswer = PatientAnswer
                    .builder()
                    .questionId(data.getQuestionId())
                    .answer(data.getAnswer())
                    .build();

            if (a.getCanWriteDesc() && data.getDesc() != null &&
                    !data.getDesc().isEmpty())
                patientAnswer.setDesc(data.getDesc());

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

        PatientForm newPatientForm = PatientForm
                .builder()
                .subModuleId(subModuleId)
                .answers(patientAnswers)
                .build();

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
                userId.equals(area.getOwnerId()) ? null : userId
        );

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(
                areaId, patientId
        ).orElseThrow(InvalidIdException::new);

        Module wantedModule = moduleRepository.findById(moduleId).orElseThrow(RuntimeException::new);
        SubModule wantedSubModule = wantedModule.getSubModules().stream().filter(subModule -> subModule.getId().equals(subModuleId))
                .findFirst().orElseThrow(RuntimeException::new);

        ObjectId wantedModuleId = moduleId;
        ObjectId wantedSubModuleId = subModuleId;

        if(wantedSubModule.getReadOnlyModuleId() != null &&
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

        if (optionalPatientReferral.isEmpty())
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
                                .answer("___mark___" + wantedPatientForm.getMark().get(key))
                                .build()
                );

        return new ResponseEntity<>(
                answers,
                HttpStatus.OK
        );
    }
}
