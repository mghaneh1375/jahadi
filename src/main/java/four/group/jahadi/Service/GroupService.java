package four.group.jahadi.Service;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.DTO.UserAccessInGroupFacetDto;
import four.group.jahadi.Enums.Access;
import four.group.jahadi.Exception.BadRequestException;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Repository.*;
import four.group.jahadi.Repository.Area.PatientsInAreaRepository;
import four.group.jahadi.Utility.FileUtils;
import four.group.jahadi.Utility.Utility;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static four.group.jahadi.Service.UserService.PICS_FOLDER;
import static four.group.jahadi.Utility.FileUtils.removeFile;
import static four.group.jahadi.Utility.FileUtils.uploadFile;
import static four.group.jahadi.Utility.StaticValues.ONE_MB;


@Service
@RequiredArgsConstructor
public class GroupService extends AbstractService<Group, GroupData> {

    private final GroupRepository groupRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final TripService tripService;
    private final ExternalReferralRepository externalReferralRepository;
    private final PatientsInAreaRepository patientsInAreaRepository;
    private final WareHouseAccessForGroupRepository wareHouseAccessForGroupRepository;

    public static void fillGroupByUsers(List<Group> groups, UserRepository userRepository) {
        List<ObjectId> userIds = groups.stream().map(Group::getOwner).collect(Collectors.toList());
        List<User> users = userRepository.findGroupUsersByIdsIn(userIds);
        groups.forEach(x -> x.setUser(users.stream().filter(itr -> x.getOwner().equals(itr.getId())).findFirst().orElse(null)));
    }


    public ResponseEntity<Page<Group>> list(Integer pageIndex, Integer pageSize, String searchKey) {
        if(searchKey == null)
            searchKey = ".*";

        Page<Group> groups = groupRepository.findLikeName(searchKey, Pageable.ofSize(pageSize).withPage(pageIndex));

        fillGroupByUsers(groups.getContent(), userRepository);
        groups.forEach(group -> group.setTripsCount(tripRepository.countByGroupId(group.getId())));
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Override
    public void update(ObjectId id, GroupData dto, Object... params) {

        Group group = groupRepository.findById(id).orElseThrow(InvalidIdException::new);

        group.setOwner(dto.getOwner());
        group.setName(dto.getName());
        group.setColor(dto.getColor());

        groupRepository.save(group);
    }

    public ResponseEntity<Group> findById(ObjectId id, Object... params) {
        return new ResponseEntity<>(
                groupRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Group> store(GroupData data, Object... params) {

        if (userRepository.countActiveBy_id(data.getOwner()) == 0)
            throw new InvalidFieldsException("مسئول موردنظر وجود ندارد");

        Group group = populateEntity(new Group(), data);

        try {
            groupRepository.insert(group);
        } catch (Exception x) {
            throw new InvalidFieldsException("نام وارد شده تکراری است");
        }

        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    public void changeCode(ObjectId groupId, int code) {
        if (groupId == null)
            throw new NotAccessException();

        Group group = groupRepository.findById(groupId).orElseThrow(InvalidIdException::new);
        group.setCode(code);
        groupRepository.save(group);
    }

    public void setPic(ObjectId id, MultipartFile file) {

        if (file == null)
            throw new BadRequestException();

        if (file.getSize() > ONE_MB * 5)
            throw new RuntimeException("حداکثر حجم مجاز 5MB می باشد");

        String fileType = FileUtils.uploadImage(file);

        if (fileType == null)
            throw new RuntimeException("فرمت فایل موردنظر معتبر نمی باشد.");

        String filename = uploadFile(file, PICS_FOLDER);
        if (filename == null)
            throw new RuntimeException("خطای ناشناخته هنگام بارگداری فایل");

        Group group = groupRepository.findById(id).orElseThrow(InvalidIdException::new);

        if (group.getPic() != null && !group.getPic().isEmpty())
            removeFile(group.getPic(), PICS_FOLDER);

        group.setPic(filename);
        groupRepository.save(group);
    }

    public void toggleActivityStatus(ObjectId userId) {

        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        if (user.getTotalMembers() == null)
            throw new NotAccessException();

        if (user.getGroupId() == null) {
            int code = Utility.randIntForGroupCode();
            Optional<Group> tmp = groupRepository.findByCode(code);

            while (tmp.isPresent())
                code = Utility.randIntForGroupCode();

            Group g = Group.builder()
                    .name(user.getGroupName())
                    .code(code)
                    .build();

            g.setOwner(user.getId());
            groupRepository.insert(g);

            boolean needUpdateUser = false;
            if (!user.getAccesses().contains(Access.GROUP)) {
                user.getAccesses().add(Access.GROUP);
                needUpdateUser = true;
            }

            if (user.getGroupId() == null) {
                user.setGroupId(g.getId());
                user.setGroupName(g.getName());
                needUpdateUser = true;
            }

            if (needUpdateUser)
                userRepository.save(user);
            return;
        }

        Group group = groupRepository.findById(user.getGroupId())
                .orElseThrow(InvalidIdException::new);
        group.setActive(!group.isActive());
        groupRepository.save(group);
    }

    public void setNewOwner(ObjectId groupId, ObjectId userId) {

        User newOwner = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        Group group = groupRepository.findById(groupId).orElseThrow(InvalidIdException::new);

        Optional<User> oldOwner = userRepository.findById(group.getOwner());
        oldOwner.ifPresent(user -> {
            user.getAccesses().remove(Access.GROUP);
            userRepository.save(user);
        });

        group.setOwner(userId);
        groupRepository.save(group);

        if (!newOwner.getAccesses().contains(Access.GROUP)) {
            newOwner.getAccesses().add(Access.GROUP);
            userRepository.save(newOwner);
        }
    }

    @Cacheable(value = "groupStatisticData", key = "#groupId")
    public ResponseEntity<HashMap<String, Object>> statisticData(ObjectId groupId) {
        HashMap<String, Object> statisticData = new HashMap<>();
        List<Trip> trips = tripRepository.findByGroupId(groupId);
        List<ObjectId> areaIds = trips
                .stream()
                .map(Trip::getAreas)
                .flatMap(List::stream)
                .map(Area::getId)
                .collect(Collectors.toList());

        List<ExternalReferralService> externalServices =
                externalReferralRepository.findByAreaIds(areaIds);

        statisticData.put("members", userRepository.countByGroupId(groupId));
        statisticData.put("surgeries", 0);
        statisticData.put("postRef", patientsInAreaRepository.countByAreaIds(areaIds));
        statisticData.put("totalCosts",
                externalServices
                        .stream()
                        .mapToLong(ExternalReferralService::getTotalCost)
                        .sum()
        );
        statisticData.put("totalRefs", externalServices.size());
        statisticData.put("activeProjects", projectService.myProjectsNeedAction(groupId));
        statisticData.put("activeTrips", tripService.inProgressTripsForGroupAccess(groupId).getBody());

        return new ResponseEntity<>(statisticData, HttpStatus.OK);
    }

    public ResponseEntity<Page<UserAccessInGroupDto>> getAccesses(
            ObjectId groupId, int pageIndex, int pageSize
    ) {
        long skip = (long) pageIndex * pageSize;

        List<UserAccessInGroupFacetDto> result =
                wareHouseAccessForGroupRepository.findUserAccessesByGroupIdPaged(groupId, skip, pageSize);

        if (result.isEmpty()) {
            return ResponseEntity.ok(
                    new PageImpl<>(List.of(), PageRequest.of(pageIndex, pageSize), 0)
            );
        }

        UserAccessInGroupFacetDto facet = result.get(0);
        List<UserAccessInGroupDto> content =
                facet.getContent() == null ? List.of() : facet.getContent();

        long total = 0;
        if (facet.getTotalElements() != null && !facet.getTotalElements().isEmpty()) {
            total = facet.getTotalElements().get(0).getCount();
        }

        return ResponseEntity.ok(
                new PageImpl<>(
                        content,
                        PageRequest.of(pageIndex, pageSize),
                        total
                )
        );
    }
}
