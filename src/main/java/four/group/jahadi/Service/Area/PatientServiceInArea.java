package four.group.jahadi.Service.Area;

import four.group.jahadi.DTO.Patient.InquiryPatientData;
import four.group.jahadi.DTO.Patient.PatientData;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Patient;
import four.group.jahadi.Models.Area.PatientsInArea;
import four.group.jahadi.Repository.PatientRepository;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PatientServiceInArea {

    @Autowired
    PatientsInAreaRepository patientsInAreaRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    TripRepository tripRepository;

    public void createPatientAndAddToRegion(ObjectId userId, ObjectId areaId, PatientData patientData) {

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, new Date())
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
                .build();

        patientRepository.insert(newPatient);
        patientsInAreaRepository.insert(PatientsInArea.builder()
                .patientId(newPatient.getId())
                .areaId(areaId)
                .build()
        );
    }

    public void addPatientToRegion(ObjectId userId, ObjectId areaId, ObjectId patientId) {

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, new Date())
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
    }

    public ResponseEntity<Patient> inquiryPatient(ObjectId userId, ObjectId areaId,
                                                  InquiryPatientData patientData) {

        tripRepository.findActiveByAreaIdAndDispatcherId(areaId, userId, new Date())
                .orElseThrow(NotAccessException::new);

        Patient patient = patientRepository.findByIdentifierAndIdentifierType(
                patientData.getIdentifier(), patientData.getIdentifierType()
        ).orElseThrow(() -> {
            throw new InvalidFieldsException("کاربری یافت نشد");
        });

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
}
