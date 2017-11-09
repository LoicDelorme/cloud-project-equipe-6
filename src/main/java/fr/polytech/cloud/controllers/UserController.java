package fr.polytech.cloud.controllers;

import fr.polytech.cloud.entities.UserMongoDBEntity;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController extends AbstractController {

    public static final int DEFAULT_PAGINATION = 1;

    public static final int DEFAULT_PAGE_SIZE = 100;

    @Autowired
    private UserMongoDBDaoServices userMongoDBDaoServices;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getAllUsers() throws Exception {
        final List<UserMongoDBEntity> users = this.userMongoDBDaoServices.getAllWithLimit((DEFAULT_PAGINATION - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        return new ResponseEntity<String>(SERIALIZER.to(users), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> putAllUsers(@RequestBody String data) throws Exception {
        this.userMongoDBDaoServices.deleteAll();

        final List<UserMongoDBEntity> users = DESERIALIZER.from(data, new ArrayList<UserMongoDBEntity>() {}.getClass().getGenericSuperclass());
        for (UserMongoDBEntity user : users) {
            this.userMongoDBDaoServices.insert(user);
        }

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
        final UserMongoDBEntity user = DESERIALIZER.from(data, UserMongoDBEntity.class);
        this.userMongoDBDaoServices.update(id, user);

        final UserMongoDBEntity updatedUser = this.userMongoDBDaoServices.getOne(id);
        return new ResponseEntity<String>(SERIALIZER.to(updatedUser), new HttpHeaders(), HttpStatus.OK);
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
}