package four.group.jahadi.Service;

import four.group.jahadi.DTO.GroupData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Models.Group;
import four.group.jahadi.Models.User;
import four.group.jahadi.Repository.GroupRepository;
import four.group.jahadi.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static four.group.jahadi.Utility.Utility.generateErr;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;

@Service
public class GroupService extends AbstractService<Group, GroupData> {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<Group>> list(Object ... filters) {

        List<Group> groups = filters[0] == null ? groupRepository.findAll() :
                groupRepository.findLikeName(filters[0].toString());

        List<ObjectId> userIds = groups.stream().map(Group::getOwner).collect(Collectors.toList());
        List<User> users = userRepository.findBy_idIn(userIds);

//        return generateSuccessMsg("data", convertObjectsToJSONList(groups, users));
        return null;
    }

    @Override
    String update(ObjectId id, GroupData dto, Object ... params) {
        return null;
    }

    public ResponseEntity<Group> findById(ObjectId id, Object ... params) {
        return new ResponseEntity<>(
                groupRepository.findById(id).orElseThrow(InvalidIdException::new),
                HttpStatus.OK
        );
    }

    public String store(GroupData data, Object ... params) {

        if(userRepository.countActiveBy_id(data.getOwner()) == 0)
            return generateErr("مسئول موردنظر وجود ندارد");

        Group group = populateEntity(new Group(), data);

        try {
            groupRepository.insert(group);
        }
        catch (Exception x) {
            return generateErr("نام وارد شده تکراری است");
        }
        return generateSuccessMsg("id", group.getId());
    }

//    public String update(ObjectId id, ModuleData moduleData) {
//
//        Optional<Module> module = moduleRepository.findById(id);
//
//        if(!module.isPresent())
//            return JSON_NOT_VALID_ID;
//
//        moduleRepository.save(populateModuleEntity(module.get(), moduleData));
//        return JSON_OK;
//    }
//
//    public void remove(ObjectId id) {
//        moduleRepository.deleteById(id);
//    }

}
