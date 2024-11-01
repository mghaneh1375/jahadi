package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.ModuleForms.ExperimentalFormDTO;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.*;
import four.group.jahadi.Models.Experiment;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.ExperimentRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Repository.UserRepository;
import four.group.jahadi.Utility.PairValue;
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
public class ExperimentServiceInArea {

    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<PairValue>> list() {
        return new ResponseEntity<>(
                Arrays.stream(four.group.jahadi.Enums.Experiment.values())
                        .map(experiment ->
                                new PairValue(experiment.name(), experiment.getFaTranslate())
                        ).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    private PatientsInArea hasAccess(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId patientId
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area startedArea = findStartedArea(trip, areaId);
        findModule(
                startedArea, moduleId,
                startedArea.getOwnerId().equals(userId) ? null : userId,
                null
        );
        Module module = moduleRepository.findById(moduleId).get();
        if (!module.isCanSuggestExperiment())
            throw new NotAccessException();

        return patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);
    }

    public void suggest(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId patientId,
            ExperimentalFormDTO dto
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area startedArea = findStartedArea(trip, areaId);
        findModule(
                startedArea, moduleId,
                startedArea.getOwnerId().equals(userId) ? null : userId,
                null
        );
        Module module = moduleRepository.findById(moduleId).get();
        if (!module.isCanSuggestExperiment())
            throw new NotAccessException();

        PatientsInArea wantedPatient = patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .orElseThrow(InvalidIdException::new);

        List<PatientReferral> referrals = wantedPatient.getReferrals();
        PatientReferral wantedPatientReferral = referrals.stream()
                .filter(patientReferral -> patientReferral.getModuleId().equals(moduleId))
                .reduce((first, second) -> second)
                .orElseThrow(InvalidIdException::new);

        List<PatientExperiment> experiments =
                wantedPatientReferral.getExperiments() == null ? new ArrayList<>() :
                        wantedPatientReferral.getExperiments();

        experiments.add((PatientExperiment) PatientExperiment
                .builder()
                .doctorId(userId)
                .description(dto.getDescription())
                .experiment(dto.getExperiment())
                .build().createId()
        );
        wantedPatientReferral.setExperiments(experiments);

        startedArea
                .getModules()
                .stream()
                .filter(module1 -> module1.getModuleTabName().equals("آزمایشگاه"))
                .forEach(module1 -> referrals.add(
                        (PatientReferral) PatientReferral
                                .builder()
                                .moduleId(module1.getModuleId())
                                .desc(dto.getDescription())
                                .referBy(userId)
                                .build().createId()
                ));

        patientsInAreaRepository.save(wantedPatient);
    }

    public void removeExperiment(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId patientId,
            ObjectId experimentId
    ) {
        PatientsInArea wantedPatient = hasAccess(userId, areaId, moduleId, patientId);
        PatientReferral wantedPatientReferral = wantedPatient.getReferrals().stream()
                .filter(patientReferral ->
                        patientReferral.getModuleId().equals(moduleId) &&
                                patientReferral.getExperiments() != null &&
                                patientReferral.getExperiments().stream()
                                        .anyMatch(patientExperiment -> patientExperiment.getId().equals(experimentId))
                )
                .findFirst().orElseThrow(InvalidIdException::new);
        wantedPatientReferral.getExperiments()
                .removeIf(patientExperiment -> patientExperiment.getId().equals(experimentId));
        patientsInAreaRepository.save(wantedPatient);
    }

    public ResponseEntity<List<PatientExperiment>> getExperiments(
            ObjectId userId, ObjectId areaId,
            ObjectId moduleId, ObjectId patientId
    ) {
        PatientsInArea wantedPatient = hasAccess(userId, areaId, moduleId, patientId);
        PatientReferral wantedPatientReferral = wantedPatient.getReferrals()
                .stream()
                .filter(patientReferral ->
                        patientReferral.getModuleId().equals(moduleId)
                )
                .reduce((first, second) -> second)
                .orElseThrow(InvalidIdException::new);

        if (wantedPatientReferral.getExperiments() != null) {
            List<User> doctors = userRepository.findByIdsIn(
                    wantedPatientReferral.getExperiments()
                            .stream().map(PatientExperiment::getDoctorId)
                            .collect(Collectors.toList())
            );
            wantedPatientReferral
                    .getExperiments()
                    .forEach(experiment -> doctors
                            .stream()
                            .filter(doctor -> doctor.getId().equals(experiment.getDoctorId()))
                            .findFirst()
                            .ifPresent(user -> experiment.setDoctor(user.getName())));
        }

        return new ResponseEntity<>(
                wantedPatientReferral.getExperiments() == null ?
                        new ArrayList<>() : wantedPatientReferral.getExperiments(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<PatientExperiment>> getAllExperimentsOfPatient(
            ObjectId userId, ObjectId areaId, ObjectId patientId
    ) {
        Trip trip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);
        Area area = findStartedArea(trip, areaId);

        List<PatientExperiment> experiments = new ArrayList<>();
        patientsInAreaRepository.findByAreaIdAndPatientId(areaId, patientId)
                .ifPresent(wantedPatient -> {
                    if (wantedPatient.getReferrals() == null)
                        return;

                    List<User> doctors = userRepository.findByIdsIn(
                            wantedPatient.getReferrals()
                                    .stream()
                                    .map(PatientReferral::getExperiments)
                                    .filter(Objects::nonNull)
                                    .flatMap(Collection::stream)
                                    .map(PatientExperiment::getDoctorId)
                                    .collect(Collectors.toList())
                    );

                    wantedPatient.getReferrals().forEach(patientReferral -> {
                        if (patientReferral.getExperiments() == null)
                            return;

                        area.getModules()
                                .stream()
                                .filter(module -> module.getModuleId().equals(patientReferral.getModuleId()))
                                .findFirst()
                                .ifPresent(module -> patientReferral.getExperiments().forEach(experiment -> experiment.setModuleName(module.getModuleName())));
                        patientReferral.getExperiments().forEach(experiment -> doctors
                                .stream()
                                .filter(user -> user.getId().equals(experiment.getDoctorId()))
                                .findFirst()
                                .ifPresent(user -> experiment.setDoctor(user.getName())));
                        experiments.addAll(patientReferral.getExperiments());
                    });
                });

        return new ResponseEntity<>(experiments, HttpStatus.OK);
    }

    public ResponseEntity<List<ExperimentInArea>> list(ObjectId userId, ObjectId areaId) {

        Trip wantedTrip = tripRepository.findByAreaIdAndResponsibleId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(
                foundArea.getExperiments(),
                HttpStatus.OK
        );
    }

    public void addAllToExperimentsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Iterable<Experiment> experimentsIter = experimentRepository.findAllById(ids);
        List<ExperimentInArea> experiments = new ArrayList<>();

        while (experimentsIter.iterator().hasNext()) {

            Experiment experiment = experimentsIter.iterator().next();

            if (!experiment.getVisibility())
                throw new NotAccessException();

            experiments.add(
                    ExperimentInArea
                            .builder()
                            .title(experiment.getTitle())
                            .experimentId(experiment.getId())
                            .build()
            );
        }

        if (experiments.size() != ids.size())
            throw new RuntimeException("ids are incorrect");

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getExperiments().addAll(experiments);
        tripRepository.save(wantedTrip);
    }

    public void removeAllFromExperimentsList(ObjectId userId, ObjectId areaId, List<ObjectId> ids) {

        Trip wantedTrip = tripRepository.findByAreaIdAndOwnerId(areaId, userId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = wantedTrip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        foundArea.getExperiments()
                .removeIf(experimentInArea -> ids.contains(experimentInArea.getId()));

        tripRepository.save(wantedTrip);
    }

}
