package four.group.jahadi.Routes.API;

import four.group.jahadi.Enums.Drug.AmountOfUse;
import four.group.jahadi.Enums.Drug.HowToUse;
import four.group.jahadi.Enums.Drug.UseTime;
import four.group.jahadi.Models.*;
import four.group.jahadi.Service.CityService;
import four.group.jahadi.Service.DrugService;
import four.group.jahadi.Service.ExperimentService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "getAllAvailableExperiments")
    @ResponseBody
    public ResponseEntity<List<Experiment>> getAllAvailableExperiments() {
        return experimentService.list(true);
    }

    @GetMapping(value = "getDrugAmountOfUseOptions")
    @ResponseBody
    public ResponseEntity<AmountOfUse[]> getDrugAmountOfUseOptions() {
        return drugService.getDrugAmountOfUseOptions();
    }

    @GetMapping(value = "getDrugHowToUseOptions")
    @ResponseBody
    public ResponseEntity<HowToUse[]> getDrugHowToUseOptions() {
        return drugService.getDrugHowToUseOptions();
    }

    @GetMapping(value = "getDrugUseTimeOptions")
    @ResponseBody
    public ResponseEntity<UseTime[]> getDrugUseTimeOptions() {
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

}
