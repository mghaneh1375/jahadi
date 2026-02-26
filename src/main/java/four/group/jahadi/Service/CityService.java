package four.group.jahadi.Service;

import four.group.jahadi.Models.City;
import four.group.jahadi.Models.Country;
import four.group.jahadi.Models.State;
import four.group.jahadi.Repository.CityRepository;
import four.group.jahadi.Repository.CountryRepository;
import four.group.jahadi.Repository.StateRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CityRepository cityRepository;

    @Cacheable(cacheNames = "country")
    public ResponseEntity<List<Country>> getCountries() {
        return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.OK);
    }

    @Cacheable(cacheNames = "state", key = "#countryId")
    public ResponseEntity<List<State>> getStates(ObjectId countryId) {
        return new ResponseEntity<>(stateRepository.findByCountryId(countryId), HttpStatus.OK);
    }

    @Cacheable(cacheNames = "city", key = "#stateId")
    public ResponseEntity<List<City>> getCities(ObjectId stateId) {
        return new ResponseEntity<>(cityRepository.findByStateId(stateId), HttpStatus.OK);
    }

}
