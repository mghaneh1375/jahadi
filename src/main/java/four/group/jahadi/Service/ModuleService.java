package four.group.jahadi.Service;

import four.group.jahadi.DTO.ModuleData;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Repository.FilteringFactory;
import four.group.jahadi.Repository.ModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static four.group.jahadi.Utility.StaticValues.JSON_NOT_VALID_ID;
import static four.group.jahadi.Utility.StaticValues.JSON_OK;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public PaginatedResponse<Module> list(List<String> filters) {

//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<Module> all = moduleRepository.findAllWithFilter(Module.class,
//                FilteringFactory.parseFromParams(filters, Module.class), pageable
//        );
//
//        return PaginatedResponse.<Module>builder()
//                .currentPage(all.getNumber())
//                .totalItems(all.getTotalElements())
//                .totalPages(all.getTotalPages())
//                .items(all.getContent())
//                .hasNext(all.hasNext())
//                .build();
        return null;
    }

    public Module findById(ObjectId id) {
        Optional<Module> module = moduleRepository.findById(id);
        return module.orElse(null);
    }

    public String store(ModuleData moduleData) {
        Module newModule = moduleRepository.insert(populateModuleEntity(null, moduleData));
        return generateSuccessMsg("id", newModule.get_id());
    }

    public String update(ObjectId id, ModuleData moduleData) {

        Optional<Module> module = moduleRepository.findById(id);

        if(!module.isPresent())
            return JSON_NOT_VALID_ID;

        moduleRepository.save(populateModuleEntity(module.get(), moduleData));
        return JSON_OK;
    }

    public void remove(ObjectId id) {
        moduleRepository.deleteById(id);
    }

    private Module populateModuleEntity(Module module, ModuleData moduleData) {

        if(module == null)
            module = new Module();

        module.setName(moduleData.getName());
        module.setSection(moduleData.getSection());

        module.setCanSuggestDrug(moduleData.isCanSuggestDrug());
        module.setHasAccessToFullDocs(moduleData.isHasAccessToFullDocs());
        module.setHasAccessToUploadDoc(moduleData.isHasAccessToUploadDoc());

        return module;
    }
}
