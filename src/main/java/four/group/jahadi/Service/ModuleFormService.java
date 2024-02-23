package four.group.jahadi.Service;

import four.group.jahadi.DTO.ModuleForms.ExperimentalFormDTO;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleFormService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PatientsInAreaRepository patientsInAreaRepository;

    public void submitExperimentalForm(ObjectId areaId, ObjectId submitterId,
                                       ObjectId userId, ObjectId moduleId,
                                       ObjectId subModuleId, ExperimentalFormDTO dto
    ) {

        if (!patientsInAreaRepository.existByAreaIdAndPatientId(areaId, userId))
            throw new InvalidIdException();



    }

}
