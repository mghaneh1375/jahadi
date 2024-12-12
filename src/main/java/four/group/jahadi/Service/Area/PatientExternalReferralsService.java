package four.group.jahadi.Service.Area;

import four.group.jahadi.Enums.Module.AnswerType;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Area.PatientExternalForm;
import four.group.jahadi.Models.Area.PatientReferral;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Question.SimpleQuestion;
import four.group.jahadi.Models.SubModule;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientExternalReferralsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    PatientRepository patientRepository;


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
            ObjectId userId, ObjectId areaId
    ) {
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

    public ResponseEntity<List<PatientExternalForm>> getPatientExternalReferrals(
            ObjectId userId, ObjectId areaId,
            ObjectId patientId
    ) {
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
                .filter(patientReferral -> moduleIds.contains(patientReferral.getModuleId()))
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
                                .createdAt(Utility.convertDateToJalali(patientForm.getCreatedAt()))
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

                        patientExternalForms.add(form);
                    });
        });

        return new ResponseEntity<>(
                patientExternalForms,
                HttpStatus.OK
        );
    }
}
