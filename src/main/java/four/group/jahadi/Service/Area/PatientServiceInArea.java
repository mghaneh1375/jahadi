package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
import four.group.jahadi.Enums.Insurance;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.ModuleInArea;
import four.group.jahadi.Models.Area.PatientJoinArea;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.TripRepository;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static four.group.jahadi.Service.Area.AreaUtils.findStartedArea;

@Service
public class PatientServiceInArea {

    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;

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

            boolean isAdult = itr.getPatientInfo().getBirthDate() != null && Period.between(
                    itr.getPatientInfo().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            ).getYears() > 10;

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

        findStartedArea(trip, areaId);

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
        patientsInAreaRepository.insert(PatientsInArea.builder()
                .patientId(newPatient.getId())
                .areaId(areaId)
                .build()
        );
    }

    public void addPatientToRegion(ObjectId userId, ObjectId areaId, ObjectId patientId) {

        Trip trip = tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        findStartedArea(trip, areaId);

        if (patientRepository.findById(patientId).isEmpty())
            throw new InvalidIdException();

        if (patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new InvalidFieldsException("فرد مورد نظر پیش از این افزوده شده است");

        patientsInAreaRepository.insert(PatientsInArea.builder()
                .patientId(patientId)
                .areaId(areaId)
                .build()
        );

        //todo: add to insurance list if module exist in area
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

        if(!patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
            throw new InvalidIdException();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(InvalidIdException::new);

        patient.setInsurance(insuranceStatus);
        patientRepository.save(patient);
    }

    public ResponseEntity<List<Patient>> getMyPatients(
            ObjectId userId, ObjectId areaId, ObjectId moduleId,
            Boolean justRecepted, Boolean justUnRecepted
    ) {

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(areaId, userId, moduleId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if (!foundArea.getOwnerId().equals(userId)) {
            ModuleInArea moduleInArea = foundArea
                    .getModules().stream().filter(module -> module.getModuleId().equals(moduleId))
                    .findFirst().orElseThrow(RuntimeException::new);
            if (!moduleInArea.getMembers().contains(userId))
                throw new NotAccessException();
        }

        List<Patient> patients;

        if ((justRecepted != null && justRecepted) || (justUnRecepted != null && justUnRecepted))
            patients = patientsInAreaRepository.findByAreaIdAndModuleIdAndRecepted(
                    areaId, moduleId,
                    justRecepted != null && justRecepted
            );
        else
            patients = patientsInAreaRepository.findByAreaIdAndModuleId(
                    areaId, moduleId
            );

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
}
