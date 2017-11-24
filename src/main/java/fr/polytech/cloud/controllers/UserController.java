package fr.polytech.cloud.controllers;

import fr.polytech.cloud.controllers.responses.EmptyResponse;
import fr.polytech.cloud.entities.dao.UserDao;
import fr.polytech.cloud.entities.dto.PositionDto;
import fr.polytech.cloud.entities.dto.UserDto;
import fr.polytech.cloud.serializers.BirthDayDtoSerializer;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController extends AbstractController {

    public static final int DEFAULT_PAGE = 0;

    public static final int DEFAULT_PAGE_SIZE = 100;

    public static final int DEFAULT_NEAREST_USERS_PAGE_SIZE = 10;

    public static final int DEFAULT_COORDINATES_LON_OFFSET = 0;

    public static final int DEFAULT_COORDINATES_LAT_OFFSET = 1;

    public static final List<String> DEFAULT_SUPPORTED_OPERATORS = Arrays.asList("eq", "gt", "gte", "lte", "lt", "ne");

    public static final String DEFAULT_AGE_SEARCH_PATTERN = "{birthDay: {$%s: '%s'}}";

    public static final String DEFAULT_LAST_NAME_SEARCH_PATTERN = "{lastName: '%s'}";

    public static final String DEFAULT_LOCATION_SEARCH_PATTERN = "{position: {$near: {$geometry: {type: 'Point' , coordinates: [%s,%s]}}}}";

    public static final String DEFAULT_SORTING_CONDITION = "{lastName: -1}";

    @Autowired
    private UserMongoDBDaoServices userMongoDBDaoServices;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllUsers(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page) throws Exception {
        final List<UserDao> users = this.userMongoDBDaoServices.getAllWithLimit(computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, DEFAULT_SORTING_CONDITION, UserDao.class);
        return new ResponseEntity(users, HttpStatus.OK);
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
    public ResponseEntity putAllUsers(@RequestBody List<UserDto> data) throws Exception {
        this.userMongoDBDaoServices.deleteAll();
        this.userMongoDBDaoServices.insertAll(data.toArray(new UserDto[data.size()]));

        final List<UserDao> users = this.userMongoDBDaoServices.getAllWithLimit(DEFAULT_PAGE * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, DEFAULT_SORTING_CONDITION, UserDao.class);
        return new ResponseEntity(users, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteAllUsers() throws Exception {
        this.userMongoDBDaoServices.deleteAll();
        return new ResponseEntity(new EmptyResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getOneUser(@PathVariable String id) throws Exception {
        try {
            final UserDao user = this.userMongoDBDaoServices.getOne(id, UserDao.class);
            if (user == null) {
                return new ResponseEntity("Unknown user for the provided ID!", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity(user, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity("Invalid ID format!", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity postOneUser(@RequestBody UserDto data) throws Exception {
        this.userMongoDBDaoServices.insert(data);

        final UserDao user = this.userMongoDBDaoServices.getOne(data.getId(), UserDao.class);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity putOneUser(@PathVariable String id, @RequestBody UserDto data) throws Exception {
        try {
            final UserDao user = this.userMongoDBDaoServices.getOne(id, UserDao.class);
            if (user == null) {
                return new ResponseEntity("Unknown user for the provided ID!", HttpStatus.NOT_FOUND);
            }

            this.userMongoDBDaoServices.update(id, mergeUsersInformation(data, user));
            return new ResponseEntity(this.userMongoDBDaoServices.getOne(id, UserDao.class), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity("Invalid ID format!", HttpStatus.NOT_FOUND);
        }
    }

    private UserDto mergeUsersInformation(UserDto userDto, UserDao userDao) {
        final String id = userDto.getId() == null ? userDao.getId() : userDto.getId();
        final String lastName = userDto.getLastName() == null ? userDao.getLastName() : userDto.getLastName();
        final String firstName = userDto.getFirstName() == null ? userDao.getFirstName() : userDto.getFirstName();
        final String birthDay = userDto.getBirthDay() == null ? userDao.getBirthDay() : userDto.getBirthDay();

        final List<Double> userDaoCoordinates = userDao.getPosition() == null ? null : userDao.getPosition().getCoordinates();
        final Double userDtoLon = userDto.getPosition() == null ? null : userDto.getPosition().getLon();
        final Double userDaoLon = userDaoCoordinates == null ? null : userDaoCoordinates.get(DEFAULT_COORDINATES_LON_OFFSET);
        final Double userDtoLat = userDto.getPosition() == null ? null : userDto.getPosition().getLat();
        final Double userDaoLat = userDaoCoordinates == null ? null : userDaoCoordinates.get(DEFAULT_COORDINATES_LAT_OFFSET);
        final Double lon = userDtoLon == null ? userDaoLon : userDtoLon;
        final Double lat = userDtoLat == null ? userDaoLat : userDtoLat;

        final PositionDto position = new PositionDto();
        position.setLon(lon);
        position.setLat(lat);

        final UserDto user = new UserDto();
        user.setId(id);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBirthDay(birthDay);
        user.setPosition(position);

        return user;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteOneUser(@PathVariable String id) throws Exception {
        try {
            final UserDao user = this.userMongoDBDaoServices.getOne(id, UserDao.class);
            if (user == null) {
                return new ResponseEntity("Unknown user for the provided ID!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            this.userMongoDBDaoServices.delete(id);
            return new ResponseEntity(new EmptyResponse(), HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity("Invalid ID format!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/age", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity searchByAge(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam Map<String, String> parameters) throws Exception {
        final String operator = DEFAULT_SUPPORTED_OPERATORS.stream().filter(parameters.keySet()::contains).collect(Collectors.toList()).get(0);
        final String queryOperator = getOppositeQueryOperator(operator);
        int age;
        try {
            age = Integer.parseInt(parameters.get(operator));
        } catch (NumberFormatException ex) {
            return new ResponseEntity("Invalid age format!", HttpStatus.NOT_FOUND);
        }

        if (age <= 0) {
            return new ResponseEntity("Age must be positive!", HttpStatus.NOT_FOUND);
        }

        final List<UserDao> users = this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(DEFAULT_AGE_SEARCH_PATTERN, queryOperator, LocalDate.now().minusYears(age).format(BirthDayDtoSerializer.DATE_PATTERN_OUT)), computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, DEFAULT_SORTING_CONDITION, UserDao.class);
        return new ResponseEntity(users, HttpStatus.OK);
    }

    private String getOppositeQueryOperator(final String operator) {
        if ("gt".equals(operator)) {
            return "lt";
        } else if ("gte".equals(operator)) {
            return "lte";
        } else if ("lt".equals(operator)) {
            return "gt";
        } else if ("lte".equals(operator)) {
            return "gte";
        } else {
            return operator;
        }
    }

    @RequestMapping(value = "/user/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity searchByLastname(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam(value = "term") String term) throws Exception {
        final List<UserDao> users = this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(DEFAULT_LAST_NAME_SEARCH_PATTERN, term), computePage(page) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, DEFAULT_SORTING_CONDITION, UserDao.class);
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/nearest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity nearest(@RequestParam(defaultValue = "" + DEFAULT_PAGE, value = "page") String page, @RequestParam(value = "lon") String lon, @RequestParam(value = "lat") String lat) throws Exception {
        final List<UserDao> users = this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(DEFAULT_LOCATION_SEARCH_PATTERN, lon, lat), computePage(page) * DEFAULT_NEAREST_USERS_PAGE_SIZE, DEFAULT_NEAREST_USERS_PAGE_SIZE, UserDao.class);
        return new ResponseEntity(users, HttpStatus.OK);
    }
}