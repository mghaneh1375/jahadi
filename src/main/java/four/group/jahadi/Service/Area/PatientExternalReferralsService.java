package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.AddPatientExternalReferralsServiceDAO;
import four.group.jahadi.DTO.CallHistoryDAO;
import four.group.jahadi.DTO.PaymentsToPatientDAO;
import four.group.jahadi.DTO.SetPatientExternalReferralsTrackingStatusDAO;
import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.PatientExternalForm;
import four.group.jahadi.Models.Area.PatientForm;
import four.group.jahadi.Models.Area.PatientReferral;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientExternalReferralsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ExternalReferralAccessForGroupRepository externalReferralAccessForGroupRepository;
    @Autowired
    private ExternalReferralRepository externalReferralRepository;


    private PairValue getExternalReferralsSubModulesId() {
        List<Module> externalReferralModules = moduleRepository.findAllBySubModuleName("ارجاع به خارج");
        if (externalReferralModules.size() == 0)
            throw new RuntimeException("خطای ناشناخته");
        List<ObjectId> moduleIds = externalReferralModules
                .stream()
                .map(Module::getId)
                .collect(Collectors.toList());
        List<ObjectId> subModuleIds = externalReferralModules
                .stream()
                .map(module -> module
                        .getSubModules()
                        .stream()
                        .filter(subModule -> subModule.getName().equalsIgnoreCase("ارجاع به خارج"))
                        .map(SubModule::getId)
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new PairValue(moduleIds, subModuleIds);
    }

    private PairValue getExternalReferralsSubModules() {
        List<Module> externalReferralModules = moduleRepository.findAllBySubModuleName("ارجاع به خارج");
        if (externalReferralModules.size() == 0)
            throw new RuntimeException("خطای ناشناخته");
        List<ObjectId> moduleIds = externalReferralModules
                .stream()
                .map(Module::getId)
                .collect(Collectors.toList());
        List<SubModule> subModuleIds = externalReferralModules
                .stream()
                .map(module -> module
                        .getSubModules()
                        .stream()
                        .filter(subModule -> subModule.getName().equalsIgnoreCase("ارجاع به خارج"))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new PairValue(moduleIds, subModuleIds);
    }

    public ResponseEntity<List<Patient>> getAllExternalReferrals(
            ObjectId userId, ObjectId groupId, ObjectId areaId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        PairValue pairValue = getExternalReferralsSubModulesId();
        List<ObjectId> patientsId = patientsInAreaRepository.findByAreaIdAndModuleIdInAndSubModuleIdIn(
                        areaId, (List<ObjectId>) pairValue.getKey(), (List<ObjectId>) pairValue.getValue()
                )
                .stream()
                .map(PatientsInArea::getPatientId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                patientRepository.findAllByIds(patientsId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Boolean> hasExternalReferralAccess(ObjectId groupId, ObjectId userId) {
        return new ResponseEntity<>(
                externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PatientExternalForm>> getPatientExternalReferrals(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(
                areaId, patientId
        ).orElseThrow(InvalidIdException::new);

        PairValue pairValue = getExternalReferralsSubModules();
        List<ObjectId> moduleIds = (List<ObjectId>) pairValue.getKey();
        List<SubModule> subModules = (List<SubModule>) pairValue.getValue();
        HashMap<ObjectId, String> moduleHashMap = new HashMap<>();
        HashMap<ObjectId, String> doctorsHashMap = new HashMap<>();

        List<ObjectId> subModuleIds =
                subModules
                        .stream()
                        .map(SubModule::getId)
                        .collect(Collectors.toList());

        List<PatientReferral> referrals = patientInArea
                .getReferrals()
                .stream()
                .filter(patientReferral -> moduleIds.contains(patientReferral.getModuleId()) && patientReferral.getForms() != null && patientReferral.getForms().size() > 0)
                .collect(Collectors.toList());

        List<PatientExternalForm> patientExternalForms =
                new ArrayList<>();

        referrals.forEach(patientReferral -> {
            patientReferral
                    .getForms()
                    .stream()
                    .filter(patientForm -> subModuleIds.contains(patientForm.getSubModuleId()))
                    .forEach(patientForm -> {
                        if (!moduleHashMap.containsKey(patientReferral.getModuleId())) {
                            moduleRepository
                                    .findDigestById(patientReferral.getModuleId())
                                    .ifPresent(module -> moduleHashMap.put(
                                            patientReferral.getModuleId(), module.getName()
                                    ));
                        }
                        if (patientForm.getDoctorId() != null &&
                                !doctorsHashMap.containsKey(patientForm.getDoctorId())) {
                            userRepository
                                    .findDigestById(patientForm.getDoctorId())
                                    .ifPresent(user -> doctorsHashMap.put(
                                            patientForm.getDoctorId(), user.getName()
                                    ));
                        }

                        PatientExternalForm form = PatientExternalForm
                                .builder()
                                .referredFrom(moduleHashMap.get(patientReferral.getModuleId()))
                                .createdAt(Utility.convertUTCDateToJalali(patientForm.getCreatedAt()))
                                .doctor(
                                        patientForm.getDoctorId() == null || !doctorsHashMap.containsKey(patientForm.getDoctorId())
                                                ? "" : doctorsHashMap.get(patientForm.getDoctorId())
                                )
                                .build();

                        SubModule subModule = subModules
                                .stream()
                                .filter(subModule1 -> subModule1.getId().equals(patientForm.getSubModuleId()))
                                .findFirst()
                                .get();

                        subModule
                                .getQuestions()
                                .stream()
                                .filter(question -> ((SimpleQuestion) question).getAnswerType().equals(AnswerType.TEXT))
                                .findFirst()
                                .ifPresent(question -> {
                                    patientForm
                                            .getAnswers()
                                            .stream()
                                            .filter(patientAnswer -> patientAnswer.getQuestionId().equals(question.getId()))
                                            .findFirst()
                                            .ifPresent(patientAnswer -> form.setReferredTo(patientAnswer.getAnswer().toString()));
                                });

                        subModule
                                .getQuestions()
                                .stream()
                                .filter(question -> ((SimpleQuestion) question).getAnswerType().equals(AnswerType.LONG_TEXT))
                                .findFirst()
                                .ifPresent(question -> {
                                    patientForm
                                            .getAnswers()
                                            .stream()
                                            .filter(patientAnswer -> patientAnswer.getQuestionId().equals(question.getId()))
                                            .findFirst()
                                            .ifPresent(patientAnswer -> form.setReason(patientAnswer.getAnswer().toString()));
                                });

                        if (form.getReferredTo() == null)
                            form.setReferredTo("");

                        form.setStatus(
                                patientForm.getExternalReferralTrackingStatus() == null
                                        ? "تعیین نشده"
                                        : patientForm.getExternalReferralTrackingStatus().getFaTranslate());

                        form.setStatusLastModifiedAt(patientForm.getExternalReferralTrackingStatusLastModifiedAt());
                        form.setFormId(patientForm.getId());
                        patientExternalForms.add(form);
                    });
        });

        return new ResponseEntity<>(
                patientExternalForms,
                HttpStatus.OK
        );
    }

    private Pair<ObjectId, PatientForm> findPatientForm(
            PatientsInArea patientInArea, ObjectId formId
    ) {
        PatientReferral patientReferral1 = patientInArea.getReferrals()
                .stream()
                .filter(patientReferral -> patientReferral.getForms() != null &&
                        patientReferral.getForms().size() > 0 &&
                        patientReferral.getForms()
                                .stream()
                                .map(PatientForm::getId)
                                .collect(Collectors.toList())
                                .contains(formId)
                ).findFirst().orElseThrow(NotAccessException::new);

        return Pair.of(patientReferral1.getModuleId(),
                patientReferral1.getForms()
                        .stream()
                        .filter(patientForm -> patientForm.getId().equals(formId))
                        .findFirst().get()
        );
    }

    public void setPatientExternalReferralsTrackingStatus(
            ObjectId userId, ObjectId groupId,
            SetPatientExternalReferralsTrackingStatusDAO request
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, request.getAreaId())
        )
            throw new NotAccessException();

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(
                request.getAreaId(), request.getPatientId()
        ).orElseThrow(InvalidIdException::new);

        if (patientInArea.getReferrals() == null)
            throw new NotAccessException();

        PatientForm patientForm1 = findPatientForm(
                patientInArea, request.getFormId()
        ).getSecond();
        patientForm1.setExternalReferralTrackingStatus(request.getStatus());
        patientForm1.setExternalReferralTrackingStatusLastModifiedAt(LocalDateTime.now());
        patientsInAreaRepository.save(patientInArea);
    }

    public void addPatientExternalReferralsService(
            boolean hasGroupAccess,
            ObjectId userId, ObjectId groupId,
            AddPatientExternalReferralsServiceDAO request
    ) {
        if ((!hasGroupAccess &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, request.getAreaId())
        )
            throw new NotAccessException();

        PatientsInArea patientInArea = patientsInAreaRepository.findByAreaIdAndPatientId(
                request.getAreaId(), request.getPatientId()
        ).orElseThrow(InvalidIdException::new);

        if (patientInArea.getReferrals() == null)
            throw new NotAccessException();

        Pair<ObjectId, PatientForm> pair = findPatientForm(
                patientInArea, request.getFormId()
        );

        ExternalReferralService externalReferralService = ExternalReferralService
                .builder()
                .patientId(request.getPatientId())
                .formId(pair.getSecond().getId())
                .areaId(request.getAreaId())
                .moduleId(pair.getFirst())
                .createdBy(userId)
                .service(request.getService())
                .totalCost(request.getTotalCost())
                .userPaid(request.getUserPaid())
                .hospitalCost(request.getHospitalCost())
                .jahadiPaid(request.getJahadiPaid())
                .date(request.getDate())
                .location(request.getLocation())
                .comeDescription(request.getComeDescription())
                .notComingDescription(request.getNotComingDescription())
                .comeCallingCount(request.getComeCallingCount())
                .notComingCallingCount(request.getNotComingCallingCount())
                .build();

        externalReferralRepository.insert(externalReferralService);
    }

    public void removePatientExternalReferralsService(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId, ObjectId serviceId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        externalReferralRepository.removeService(patientId, areaId, formId, serviceId);
    }

    public ResponseEntity<List<ExternalReferralService>> getPatientExternalReferralsServices(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        return new ResponseEntity<>(
                externalReferralRepository.findServices(patientId, areaId, formId),
                HttpStatus.OK
        );
    }

    public void addPatientExternalReferralsServicesFinanceHistory(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId, ObjectId serviceId,
            PaymentsToPatientDAO request
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        ExternalReferralService service = externalReferralRepository.findService(patientId, areaId, formId, serviceId)
                .orElseThrow(InvalidIdException::new);

        List<PaymentsToPatient> payments;
        if (service.getPayments() == null)
            payments = new ArrayList<>();
        else
            payments = service.getPayments();

        payments.add((PaymentsToPatient) PaymentsToPatient
                .builder()
                .action(request.getAction())
                .amount(request.getAmount())
                .cardNo(request.getCardNo())
                .date(request.getDate())
                .build().createId()
        );

        service.setPayments(payments);
        externalReferralRepository.save(service);
    }

    public void removePatientExternalReferralsServicesFinanceHistory(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId, ObjectId serviceId,
            ObjectId financeId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        ExternalReferralService service = externalReferralRepository.findService(patientId, areaId, formId, serviceId)
                .orElseThrow(InvalidIdException::new);

        if (service.getPayments() == null)
            throw new NotAccessException();

        int s = service.getPayments().size();
        service.getPayments().removeIf(paymentsToPatient -> paymentsToPatient.getId().equals(financeId));

        if (s != service.getPayments().size())
            externalReferralRepository.save(service);
    }


    public void addPatientExternalReferralsServicesCallHistory(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId, ObjectId serviceId,
            CallHistoryDAO request
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        ExternalReferralService service = externalReferralRepository.findService(patientId, areaId, formId, serviceId)
                .orElseThrow(InvalidIdException::new);

        List<CallHistory> callHistories;
        if (service.getPayments() == null)
            callHistories = new ArrayList<>();
        else
            callHistories = service.getCallHistories();

        callHistories.add((CallHistory) CallHistory
                .builder()
                .answered(request.getAnswered())
                .counter(request.getCounter())
                .description(request.getDescription())
                .build().createId()
        );

        service.setCallHistories(callHistories);
        externalReferralRepository.save(service);
    }

    public void removePatientExternalReferralsServicesCallHistory(
            ObjectId userId, ObjectId groupId,
            ObjectId areaId, ObjectId patientId,
            ObjectId formId, ObjectId serviceId,
            ObjectId callId
    ) {
        if ((userId != null &&
                !externalReferralAccessForGroupRepository.existsAccessByGroupIdAndUserId(groupId, userId)) ||
                !tripRepository.existByGroupIdAndAreaId(groupId, areaId)
        )
            throw new NotAccessException();

        ExternalReferralService service = externalReferralRepository.findService(patientId, areaId, formId, serviceId)
                .orElseThrow(InvalidIdException::new);

        if (service.getCallHistories() == null)
            throw new NotAccessException();

        int s = service.getCallHistories().size();
        service.getCallHistories().removeIf(callHistory -> callHistory.getId().equals(callId));

        if (s != service.getCallHistories().size())
            externalReferralRepository.save(service);
    }
}
