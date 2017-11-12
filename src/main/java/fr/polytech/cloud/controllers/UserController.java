package fr.polytech.cloud.controllers;

import fr.polytech.cloud.entities.UserMongoDBEntity;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController extends AbstractController {

    public static final int DEFAULT_PAGE = 0;

    public static final int DEFAULT_PAGE_SIZE = 100;

    public static final int DEFAULT_NEAREST_PAGE_SIZE = 10;

    public static final int DEFAULT_COORDINATES_LON_OFFSET = 0;

    public static final int DEFAULT_COORDINATES_LAT_OFFSET = 1;

    public static final int DEFAULT_COORDINATES_SIZE = 2;

    public static final List<String> DEFAULT_SUPPORTED_OPERATORS = Arrays.asList("eq", "gt", "gte", "lt", "lte", "ne");

    public static final int DEFAULT_SUPPORTED_OPERATORS_NUMBER = 1;

    public static final String DEFAULT_AGE_SEARCH_PATTERN = "{birthDay : {$%s: #}}";

    public static final String DEFAULT_LASTNAME_SEARCH_PATTERN = "{lastName : '%s'}";

    public static final String DEFAULT_LOCATION_SEARCH_PATTERN = "{position: {$near: {$geometry: {type: \"Point\" , coordinates: [%s,%s]}}}}";

    @Autowired
    private UserMongoDBDaoServices userMongoDBDaoServices;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getAllUsers(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page) throws Exception {
        final List<UserMongoDBEntity> users = this.userMongoDBDaoServices.getAllWithLimit(computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.OK);
    }

    private int computePage(String requestedPage) {
        int page;
        try {
            page = Integer.parseInt(requestedPage);
        } catch (NumberFormatException ex) {
            page = DEFAULT_PAGE;
        }

        return page;
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> putAllUsers(@RequestBody String data) throws Exception {
        this.userMongoDBDaoServices.deleteAll();

        final UserMongoDBEntity[] users = DESERIALIZER.from(data, UserMongoDBEntity[].class);
        this.userMongoDBDaoServices.insertAll(users);

        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> deleteAllUsers() throws Exception {
        this.userMongoDBDaoServices.deleteAll();
        return new ResponseEntity<String>(new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getOneUser(@PathVariable String id) throws Exception {
        final UserMongoDBEntity user = this.userMongoDBDaoServices.getOne(id);
        if (user == null) {
            return new ResponseEntity<String>("Unknown user!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(SERIALIZER.to(user), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> postOneUser(@RequestBody String data) throws Exception {
        final UserMongoDBEntity user = DESERIALIZER.from(data, UserMongoDBEntity.class);
        this.userMongoDBDaoServices.insert(user);

        return new ResponseEntity<String>(SERIALIZER.to(user), new HttpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> putOneUser(@PathVariable String id, @RequestBody String data) throws Exception {
        final UserMongoDBEntity user = this.userMongoDBDaoServices.getOne(id);
        if (user == null) {
            return new ResponseEntity<String>("Unknown user!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        mergeUserMongoDBEntityFromInto(DESERIALIZER.from(data, UserMongoDBEntity.class), user);
        this.userMongoDBDaoServices.update(id, user);

        return new ResponseEntity<String>(SERIALIZER.to(user), new HttpHeaders(), HttpStatus.OK);
    }

    private void mergeUserMongoDBEntityFromInto(UserMongoDBEntity from, UserMongoDBEntity into) {
        final String id = from.getId() == null ? into.getId() : from.getId();
        final String lastName = from.getLastName() == null ? into.getLastName() : from.getLastName();
        final String firstName = from.getFirstName() == null ? into.getFirstName() : from.getFirstName();
        final Date birthDay = from.getBirthDay() == null ? into.getBirthDay() : from.getBirthDay();

        final List<Double> fromCoordinates = from.getPosition() == null ? null : from.getPosition().getCoordinates();
        final List<Double> intoCoordinates = into.getPosition() == null ? null : into.getPosition().getCoordinates();
        final Double fromLon = (fromCoordinates == null || fromCoordinates.size() != DEFAULT_COORDINATES_SIZE) ? null : fromCoordinates.get(DEFAULT_COORDINATES_LON_OFFSET);
        final Double intoLon = (intoCoordinates == null || intoCoordinates.size() != DEFAULT_COORDINATES_SIZE) ? null : intoCoordinates.get(DEFAULT_COORDINATES_LON_OFFSET);
        final Double fromLat = (fromCoordinates == null || fromCoordinates.isEmpty()) ? null : fromCoordinates.get(DEFAULT_COORDINATES_LAT_OFFSET);
        final Double intoLat = (intoCoordinates == null || intoCoordinates.isEmpty()) ? null : intoCoordinates.get(DEFAULT_COORDINATES_LAT_OFFSET);
        final Double lon = fromLon == null ? intoLon : fromLon;
        final Double lat = fromLat == null ? intoLat : fromLat;

        into.setId(id);
        into.setLastName(lastName);
        into.setFirstName(firstName);
        into.setBirthDay(birthDay);

        intoCoordinates.clear();
        intoCoordinates.add(lon);
        intoCoordinates.add(lat);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> deleteOneUser(@PathVariable String id) throws Exception {
        final UserMongoDBEntity user = this.userMongoDBDaoServices.getOne(id);
        if (user == null) {
            return new ResponseEntity<String>("Unknown user!", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        this.userMongoDBDaoServices.delete(id);
        return new ResponseEntity<String>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/user/age", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> searchByAge(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam Map<String, String> parameters) throws Exception {
        final List<String> requestedOperators = DEFAULT_SUPPORTED_OPERATORS.stream().filter(parameters.keySet()::contains).collect(Collectors.toList());
        if (requestedOperators.size() != DEFAULT_SUPPORTED_OPERATORS_NUMBER) {
            return new ResponseEntity<String>("Only one operator is supported at the same time!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        final String requestedOperator = requestedOperators.get(0);
        int age;
        try {
            age = Integer.parseInt(parameters.get(requestedOperator));
        } catch (NumberFormatException ex) {
            return new ResponseEntity<String>("Invalid age number!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        final LocalDate computedDate = LocalDate.now().minusYears(age);

        final String query = String.format(DEFAULT_AGE_SEARCH_PATTERN, requestedOperator);
        final Date date = new Date(computedDate.getYear(), computedDate.getMonthValue(), computedDate.getDayOfMonth());

        final List<UserMongoDBEntity> users = this.userMongoDBDaoServices.getAllWhereWithLimit(query, computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, date);
        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> searchByLastname(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam(value = "term") String term) throws Exception {
        final String query = String.format(DEFAULT_LASTNAME_SEARCH_PATTERN, term);

        final List<UserMongoDBEntity> users = this.userMongoDBDaoServices.getAllWhereWithLimit(query, computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/nearest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> nearest(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam(value = "lon") String lon, @RequestParam(value = "lat") String lat) throws Exception {
        final String query = String.format(DEFAULT_LOCATION_SEARCH_PATTERN, lon, lat);

        final List<UserMongoDBEntity> users = this.userMongoDBDaoServices.getAllWhereWithLimit(query, computePage(page) * DEFAULT_NEAREST_PAGE_SIZE, DEFAULT_NEAREST_PAGE_SIZE);
        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.OK);
    }
}