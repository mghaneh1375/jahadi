package four.group.jahadi.Routes.API;

import four.group.jahadi.Models.*;
import four.group.jahadi.Models.Module;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Service.Area.AreaService;
import four.group.jahadi.Service.CityService;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Service.ExperimentService;
import four.group.jahadi.Utility.PairValue;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/public")
@Validated
public class PublicAPIRoutes {

    @Autowired
    private CityService cityService;
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private DrugService drugService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ModuleRepository moduleRepository;

    @GetMapping(value = "getAllAvailableExperiments")
    @ResponseBody
    public ResponseEntity<List<Experiment>> getAllAvailableExperiments() {
        return experimentService.list(true);
    }

    @GetMapping(value = "getDrugAmountOfUseOptions")
    @ResponseBody
    public ResponseEntity<List<PairValue>> getDrugAmountOfUseOptions() {
        return drugService.getDrugAmountOfUseOptions();
    }

    @GetMapping(value = "getDrugHowToUseOptions")
    @ResponseBody
    public ResponseEntity<List<PairValue>> getDrugHowToUseOptions() {
        return drugService.getDrugHowToUseOptions();
    }

    @GetMapping(value = "getDrugUseTimeOptions")
    @ResponseBody
    public ResponseEntity<List<PairValue>> getDrugUseTimeOptions() {
        return drugService.getDrugUseTimeOptions();
    }

    @GetMapping(value = "getAllDrugs")
    @ResponseBody
    @Operation(summary = "گرفتن اطلاعات مختصر داروها و یا سرچ در داروها برای عموم", description = "پارامتر نام دارو که میتواند بخشی از نام دارو هم باشد اختیاری و برای سرچ کردن است که باید حداقل سه کاراکتر باشد")
    public ResponseEntity<List<Drug>> getAllDrugs(@RequestParam(required = false, value = "name") String name) {

        if (name != null && name.length() > 2)
            return drugService.list(false, name);

        return drugService.list(false);
    }

    @GetMapping(value = "getDrug/{drugId}")
    @ResponseBody
    @Operation(summary = "گرفتن اطلاعات تکمیلی دارو مثل طریقه مصرف و ...")
    public ResponseEntity<Drug> getDrug(@PathVariable @ObjectIdConstraint ObjectId drugId) {
        return drugService.findById(drugId);
    }

    @GetMapping(value = "getDrugReplacements/{drugId}")
    @ResponseBody
    @Operation(summary = "گرفتن داروهای جایگزین یک دارو خاص")
    public ResponseEntity<List<Drug>> getDrugReplacements(@PathVariable @ObjectIdConstraint ObjectId drugId) {
        return drugService.findReplacements(drugId);
    }

    @GetMapping(value = "/getCountries")
    @ResponseBody
    public ResponseEntity<List<Country>> getCountries() {
        return cityService.getCountries();
    }


    @GetMapping(value = "/getStates/{countryId}")
    @ResponseBody
    public ResponseEntity<List<State>> getStates(@PathVariable @ObjectIdConstraint ObjectId countryId) {
        return cityService.getStates(countryId);
    }

    @GetMapping(value = "/getCities/{stateId}")
    @ResponseBody
    public ResponseEntity<List<City>> getCities(@PathVariable @ObjectIdConstraint ObjectId stateId) {
        return cityService.getCities(stateId);
    }

    @PostMapping(
            value = "importDBToConstructLocalServer",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    @Operation(summary = "ایمپورت کردن کل دیتابیس")
    public void importDBToConstructLocalServer(
            @RequestBody MultipartFile file
    ) {
        areaService.importDBToConstructLocalServer(file);
    }

    @GetMapping(value = "test")
    @ResponseBody
    public void test() {
        Module module = moduleRepository.findById(new ObjectId("66a54a9ae7769906d6fb63bc")).get();
        module.getSubModules().stream().filter(subModule -> subModule.getId().equals(new ObjectId("6841f4ce9f06e166a2210f05")))
                .findFirst().ifPresent(subModule -> subModule.getQuestions().stream().filter(question -> question.getId().equals(new ObjectId("6841f4ce9f06e166a2210f06"))).findFirst().ifPresent(question -> {
                    System.out.println(question);
                }));
    }
}
