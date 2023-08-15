//package four.group.jahadi.Service;
//
//import four.group.jahadi.Models.Group;
//import four.group.jahadi.Models.PaginatedResponse;
//import four.group.jahadi.Repository.FilteringFactory;
//import four.group.jahadi.Repository.GroupRepository;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//import static four.group.jahadi.Utility.Utility.generateSuccessMsg;
//
//@Service
//public class GroupService {
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    public PaginatedResponse<Group> list(List<String> filters) {
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<Group> all = groupRepository.findAllWithFilter(Group.class,
//                FilteringFactory.parseFromParams(filters, Group.class), pageable
//        );
//
//        return PaginatedResponse.<Group>builder()
//                .currentPage(all.getNumber())
//                .totalItems(all.getTotalElements())
//                .totalPages(all.getTotalPages())
//                .items(all.getContent())
//                .hasNext(all.hasNext())
//                .build();
//    }
//
//    public Group findById(ObjectId id) {
//        Optional<Group> drug = groupRepository.findById(id);
//        return drug.orElse(null);
//    }
//
//    public String store(DrugData data) {
//        Drug drug = drugRepository.insert(populateModuleEntity(null, data));
//        return generateSuccessMsg("id", drug.get_id());
//    }
//
////    public String update(ObjectId id, ModuleData moduleData) {
////
////        Optional<Module> module = moduleRepository.findById(id);
////
////        if(!module.isPresent())
////            return JSON_NOT_VALID_ID;
////
////        moduleRepository.save(populateModuleEntity(module.get(), moduleData));
////        return JSON_OK;
////    }
////
////    public void remove(ObjectId id) {
////        moduleRepository.deleteById(id);
////    }
//
//    private Drug populateModuleEntity(Drug drug, DrugData data) {
//
//        if(drug == null)
//            drug = new Drug();
//
//        drug.setName(data.getName());
//        drug.setPrice(data.getPrice());
//
//        return drug;
//    }
//}
