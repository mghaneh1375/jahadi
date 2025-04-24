package four.group.jahadi.Service.Area;

import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Area.AreaDrugs;
import four.group.jahadi.Models.Area.AreaEquipments;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.AreaDrugsRepository;
import four.group.jahadi.Repository.Area.AreaEquipmentsRepository;
import four.group.jahadi.Repository.Area.PatientsDrugRepository;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Service.IOService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportUtils {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final DrugRepository drugRepository;
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
    private final IOService ioService;

    public ExportUtils(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            DrugRepository drugRepository,
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
            IOService ioService
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.drugRepository = drugRepository;
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
        this.ioService = ioService;
    }

    public void exportUsers(Area area, ServletOutputStream outputStream) {
        List<ObjectId> neededUsersId = new ArrayList<>();
        neededUsersId.addAll(area.getMembers());
        neededUsersId.add(area.getOwnerId());
//        List<User> users = userRepository.findFullInfoByIdsIn(neededUsersId);
        List<User> users = userRepository.findAll();
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

    public void exportCommon(ServletOutputStream outputStream) {
        try {
            ioService.export(countryRepository.findAll(), outputStream, "Country");
            ioService.export(stateRepository.findAll(), outputStream, "State");
            ioService.export(cityRepository.findAll(), outputStream, "City");
            ioService.export(noteRepository.findAll(), outputStream, "Note");
            ioService.export(moduleRepository.findAll(), outputStream, "Module");
        }
        catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public void exportTrip(Trip trip, ObjectId areaId, ServletOutputStream outputStream) {
        try {
            ioService.export(Collections.singletonList(projectRepository.findById(trip.getProjectId()).get()), outputStream, "Project");
            trip.setAreas(trip.getAreas().stream().filter(area -> area.getId().equals(areaId)).collect(Collectors.toList()));
            ioService.export(Collections.singletonList(trip), outputStream, "Trip");
        }
        catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public void exportDrugs(Area area, ServletOutputStream outputStream) {
        List<AreaDrugs> drugsInArea = areaDrugsRepository.findByAreaId(area.getId());
        ioService.export(drugRepository.findFullInfoByIds(drugsInArea.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList())), outputStream, "Drug");
        ioService.export(drugBookmarkRepository.findByDrugIds(drugsInArea.stream().map(AreaDrugs::getDrugId).collect(Collectors.toList())), outputStream, "DrugBookmark");
        ioService.export(drugsInArea, outputStream, "AreaDrugs");
    }

    public void exportPatients(ServletOutputStream outputStream) {
        ioService.export(patientRepository.findAll(), outputStream, "Patient");
        ioService.export(patientsDrugRepository.findAll(), outputStream, "PatientDrug");
        ioService.export(patientsInAreaRepository.findAll(), outputStream, "PatientsInArea");
    }

    public void exportEquipments(Area area, ServletOutputStream outputStream) {
        List<AreaEquipments> equipmentsInArea = areaEquipmentsRepository.findByArea(area.getId());
        List<Equipment> equipments = equipmentRepository.findFullInfoByIds(equipmentsInArea.stream().map(AreaEquipments::getEquipmentId).collect(Collectors.toList()));
        ioService.export(equipments, outputStream, "Equipment");
        ioService.export(equipmentsInArea, outputStream, "AreaEquipments");
    }
}
