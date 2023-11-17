package four.group.jahadi.Routes.API;

import four.group.jahadi.Models.City;
import four.group.jahadi.Models.Country;
import four.group.jahadi.Models.State;
import four.group.jahadi.Service.CityService;
import four.group.jahadi.Validator.ObjectIdConstraint;
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

    @GetMapping(value = "/getCountries")
    @ResponseBody
    public ResponseEntity<List<Country>> getCountries() {
        return cityService.getCountries();
    }


    @GetMapping(value = "/getStates/{stateId}")
    @ResponseBody
    public ResponseEntity<List<State>> getStates(@PathVariable @ObjectIdConstraint ObjectId stateId) {
        return cityService.getStates(stateId);
    }

    @GetMapping(value = "/getCities/{cityId}")
    @ResponseBody
    public ResponseEntity<List<City>> getCities(@PathVariable @ObjectIdConstraint ObjectId cityId) {
        return cityService.getCities(cityId);
    }

}
