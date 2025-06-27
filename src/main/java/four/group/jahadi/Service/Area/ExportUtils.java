package four.group.jahadi.Service.Area;

import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Models.GroupAccess;
import four.group.jahadi.Models.Trip;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.Area.*;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Service.IOService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportUtils {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final DrugRepository drugRepository;
    private final DrugLogRepository drugLogRepository;
    private final DrugBookmarkRepository drugBookmarkRepository;
    private final AreaDrugsRepository areaDrugsRepository;
    private final GroupRepository groupRepository;
    private final EquipmentRepository equipmentRepository;
    private final AreaEquipmentsRepository areaEquipmentsRepository;
    private final PatientsInAreaRepository patientsInAreaRepository;
    private final PatientsDrugRepository patientsDrugRepository;
    private final PatientRepository patientRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final NoteRepository noteRepository;
    private final WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;
    private final ModuleRepository moduleRepository;
    private final PresenceListRepository presenceListRepository;
    private final IOService ioService;

    public ExportUtils(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            DrugRepository drugRepository,
            DrugLogRepository drugLogRepository,
            DrugBookmarkRepository drugBookmarkRepository,
            AreaDrugsRepository areaDrugsRepository,
            GroupRepository groupRepository,
            EquipmentRepository equipmentRepository,
            AreaEquipmentsRepository areaEquipmentsRepository,
            PatientsInAreaRepository patientsInAreaRepository,
            PatientsDrugRepository patientsDrugRepository,
            PatientRepository patientRepository,
            CityRepository cityRepository,
            StateRepository stateRepository,
            CountryRepository countryRepository,
            NoteRepository noteRepository,
            WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository,
            ModuleRepository moduleRepository,
            PresenceListRepository presenceListRepository,
            IOService ioService
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.drugRepository = drugRepository;
        this.drugLogRepository = drugLogRepository;
        this.drugBookmarkRepository = drugBookmarkRepository;
        this.areaDrugsRepository = areaDrugsRepository;
        this.groupRepository = groupRepository;
        this.equipmentRepository = equipmentRepository;
        this.areaEquipmentsRepository = areaEquipmentsRepository;
        this.patientsInAreaRepository = patientsInAreaRepository;
        this.patientsDrugRepository = patientsDrugRepository;
        this.patientRepository = patientRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.noteRepository = noteRepository;
        this.wareHouseAccessForGroupRepository = wareHouseAccessForGroupRepository;
        this.moduleRepository = moduleRepository;
        this.presenceListRepository = presenceListRepository;
        this.ioService = ioService;
    }

    public void exportUsers(Area area, ServletOutputStream outputStream, List<ObjectId> neededUsersId) {
        List<User> users = userRepository.findFullInfoByIdsIn(neededUsersId);
//        List<User> users = userRepository.findAll();
        ioService.export(users, outputStream, "User");
    }

    public void exportGroups(Trip trip, ServletOutputStream outputStream) {
        List<ObjectId> groupIds = trip.getGroupsWithAccess().stream().map(GroupAccess::getGroupId).collect(Collectors.toList());
        ioService.export(
                groupRepository.findFullInfoByIds(groupIds),
                outputStream, "Group"
        );
        ioService.export(
                wareHouseAccessForGroupRepository.findByGroupIds(groupIds),
                outputStream, "WareHouseAccessForGroup"
        );
    }

    public void exportCommon(ServletOutputStream outputStream, List<ObjectId> neededUsersId) {
        try {
            ioService.export(countryRepository.findAll(), outputStream, "Country");
            ioService.export(stateRepository.findAll(), outputStream, "State");
            ioService.export(cityRepository.findAll(), outputStream, "City");
            ioService.export(noteRepository.findByUsersIdIn(neededUsersId), outputStream, "Note");
            ioService.export(moduleRepository.findAll(), outputStream, "Module");
        }
        catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public void exportAreaTrip(Trip trip, ObjectId areaId, ServletOutputStream outputStream) {
        try {
            ioService.export(Collections.singletonList(trip), outputStream, "Trip");
            ioService.export(presenceListRepository.findByAreaId(areaId), outputStream, "PresenceList");
        }
        catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public void exportAreaDrugs(Area area, ServletOutputStream outputStream) {
        List<AreaDrugs> drugsInArea = areaDrugsRepository.findByAreaId(area.getId());
        ioService.export(drugBookmarkRepository.findByDrugIds(drugsInArea.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList())), outputStream, "DrugBookmark");
        ioService.export(drugsInArea, outputStream, "DrugsInArea");
    }

    public void exportPatients(ServletOutputStream outputStream) {
        ioService.export(patientRepository.findAll(), outputStream, "Patient");
        ioService.export(patientsDrugRepository.findAll(), outputStream, "PatientDrug");
        ioService.export(patientsInAreaRepository.findAll(), outputStream, "PatientsInArea");
    }

    public void exportEquipments(Area area, ServletOutputStream outputStream) {
        List<AreaEquipments> equipmentsInArea = areaEquipmentsRepository.findByArea(area.getId());
        ioService.export(equipmentsInArea, outputStream, "EquipmentsInArea");
    }
}
