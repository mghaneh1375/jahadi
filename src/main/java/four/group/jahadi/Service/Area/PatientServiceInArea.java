package four.group.jahadi.Service.Area;

import com.google.common.base.CaseFormat;
import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
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
import org.bson.Document;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.mapAll;

@Service
public class PatientServiceInArea {

    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    ModelMapper modelMapper;

    public ResponseEntity<List<PatientJoinArea>> getPatients(ObjectId userId, ObjectId areaId) {

        //todo: check finalize

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        return new ResponseEntity<>(
                patientsInAreaRepository.findPatientsByAreaId(areaId),
                HttpStatus.OK
        );
    }

    public void createPatientAndAddToRegion(ObjectId userId, ObjectId areaId, PatientData patientData) {

        //todo: check finalize

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

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
                .build();

        patientRepository.insert(newPatient);
        patientsInAreaRepository.insert(PatientsInArea.builder()
                .patientId(newPatient.getId())
                .areaId(areaId)
                .build()
        );
    }

    public void addPatientToRegion(ObjectId userId, ObjectId areaId, ObjectId patientId) {

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, Utility.getCurrDate())
                .orElseThrow(NotAccessException::new);

        if(patientRepository.findById(patientId).isEmpty())
            throw new InvalidIdException();

        if(patientsInAreaRepository.existByAreaIdAndPatientId(areaId, patientId))
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

    public ResponseEntity<List<Patient>> getMyPatients(
            ObjectId userId, ObjectId areaId, ObjectId moduleId,
            Boolean justRecepted, Boolean justUnRecepted
    ) {

        Trip trip = tripRepository.findByAreaIdAndResponsibleIdAndModuleId(areaId, userId, moduleId)
                .orElseThrow(NotAccessException::new);

        Area foundArea = trip
                .getAreas().stream().filter(area -> area.getId().equals(areaId))
                .findFirst().orElseThrow(RuntimeException::new);

        if(!foundArea.getOwnerId().equals(userId)) {
            ModuleInArea moduleInArea = foundArea
                    .getModules().stream().filter(module -> module.getModuleId().equals(moduleId))
                    .findFirst().orElseThrow(RuntimeException::new);
            if(!moduleInArea.getMembers().contains(userId))
                throw new NotAccessException();
        }

        List<Patient> patients;

        if((justRecepted != null && justRecepted) || (justUnRecepted != null && justUnRecepted))
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
