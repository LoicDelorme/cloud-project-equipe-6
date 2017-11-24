package fr.polytech.cloud;

import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.dao.PositionDao;
import fr.polytech.cloud.entities.dao.UserDao;
import fr.polytech.cloud.entities.dto.PositionDto;
import fr.polytech.cloud.entities.dto.UserDto;
import fr.polytech.cloud.serializers.BirthDayDtoSerializer;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyOrderStrategy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@EnableWebMvc
public class UserControllerTest {

    @Mock
    private UserMongoDBDaoServices userMongoDBDaoServices;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final Jsonb jsonSerializer = JsonbBuilder.create(new JsonbConfig().withNullValues(true).withPropertyOrderStrategy(PropertyOrderStrategy.ANY));

    private final UserDao firstUser = new UserDao();

    private final UserDao secondUser = new UserDao();

    final List<UserDao> users = new ArrayList<UserDao>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController).build();

        // Create the first user
        final PositionDao firstPosition = new PositionDao();
        firstPosition.setType("Point");
        firstPosition.setCoordinates(Arrays.asList(46.2, 45.2));

        this.firstUser.setId("5a0c11c0c98029367ea329e8");
        this.firstUser.setLastName("FIRST");
        this.firstUser.setFirstName("user");
        this.firstUser.setBirthDay("11/19/2000");
        this.firstUser.setPosition(firstPosition);

        // Create the second user
        final PositionDao secondPosition = new PositionDao();
        secondPosition.setType("Point");
        secondPosition.setCoordinates(Arrays.asList(20.28, 21.24));

        this.secondUser.setId("5a0c11c0c98029367b789745");
        this.secondUser.setLastName("SECOND");
        this.secondUser.setFirstName("user");
        this.secondUser.setBirthDay("11/19/2017");
        this.secondUser.setPosition(secondPosition);

        this.users.add(this.firstUser);
        this.users.add(this.secondUser);
    }

    @Test
    public void testGetAllUsersWithoutUsers() throws Exception {
        final List<UserDao> expectedUsers = new ArrayList<UserDao>();

        when(this.userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(content().string(this.jsonSerializer.toJson(expectedUsers)));
    }

    @Test
    public void testGetAllUsersWithUsers() throws Exception {
        final List<UserDao> expectedUsers = this.users;

        when(this.userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(content().string("[{\"id\":\"5a0c11c0c98029367ea329e8\",\"lastName\":\"FIRST\",\"firstName\":\"user\",\"birthDay\":\"11/19/2000\",\"position\":{\"lat\":45.2,\"lon\":46.2}},{\"id\":\"5a0c11c0c98029367b789745\",\"lastName\":\"SECOND\",\"firstName\":\"user\",\"birthDay\":\"11/19/2017\",\"position\":{\"lat\":21.24,\"lon\":20.28}}]"));
    }

    @Test
    public void testGetAllUsersWithUsersWithPagination() throws Exception {
        final List<UserDao> expectedUsers = new ArrayList<UserDao>();

        when(this.userMongoDBDaoServices.getAllWithLimit(1 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user").param("page", "1")).andExpect(status().isOk()).andExpect(content().string(this.jsonSerializer.toJson(expectedUsers)));
    }

    @Test
    public void testGetOneUserWithCorrectId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(this.firstUser);
        this.mockMvc.perform(get("/user/5a0c11c0c98029367ea329e8")).andExpect(status().isOk()).andExpect(content().string("{\"id\":\"5a0c11c0c98029367ea329e8\",\"lastName\":\"FIRST\",\"firstName\":\"user\",\"birthDay\":\"11/19/2000\",\"position\":{\"lat\":45.2,\"lon\":46.2}}"));
    }

    @Test
    public void testGetOneUserWithUnknownId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea32955", UserDao.class)).thenReturn(null);
        this.mockMvc.perform(get("/user/5a0c11c0c98029367ea32955")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetOneUserWithInvalidId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("fakeId", UserDao.class)).thenThrow(new IllegalArgumentException());
        this.mockMvc.perform(get("/user/fakeId")).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAllUsersWithoutUsers() throws Exception {
        when(this.userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(new ArrayList<UserDao>());
        doNothing().when(this.userMongoDBDaoServices).deleteAll();

        this.mockMvc.perform(delete("/user")).andExpect(status().isOk());
    }

    @Test
    public void testDeleteAllUsersWithUsers() throws Exception {
        when(this.userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(this.users);
        doNothing().when(this.userMongoDBDaoServices).deleteAll();

        this.mockMvc.perform(delete("/user")).andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserWithCorrectId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(this.firstUser);
        doNothing().when(this.userMongoDBDaoServices).delete("5a0c11c0c98029367ea329e8");

        this.mockMvc.perform(delete("/user/5a0c11c0c98029367ea329e8")).andExpect(status().is(204));
    }

    @Test
    public void testDeleteUserWithUnknownId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea32955", UserDao.class)).thenReturn(null);
        doNothing().when(this.userMongoDBDaoServices).delete("5a0c11c0c98029367ea32955");

        this.mockMvc.perform(delete("/user/5a0c11c0c98029367ea32955")).andExpect(status().is(500));
    }

    @Test
    public void testDeleteUserWithInvalidId() throws Exception {
        when(this.userMongoDBDaoServices.getOne("fakeId", UserDao.class)).thenThrow(new IllegalArgumentException());
        doNothing().when(this.userMongoDBDaoServices).delete("fakeId");

        this.mockMvc.perform(delete("/user/fakeId")).andExpect(status().is(500));
    }

    @Test
    public void testPutOneUserWithCorrectId() throws Exception {
        final PositionDto newPosition = new PositionDto();
        newPosition.setLat(45.56);
        newPosition.setLon(48.23);

        final UserDto newUser = new UserDto();
        newUser.setId("5a0c11c0c98029367ea329e8");
        newUser.setLastName("FIRST_");
        newUser.setFirstName("user_");
        newUser.setBirthDay("11/19/2000");
        newUser.setPosition(newPosition);

        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(this.firstUser);
        doNothing().when(this.userMongoDBDaoServices).update("5a0c11c0c98029367ea329e8", newUser);

        this.mockMvc.perform(put("/user/5a0c11c0c98029367ea329e8").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(newUser))).andExpect(status().isOk());
    }

    @Test
    public void testPutOneUserWithUnknownId() throws Exception {
        final PositionDto newPosition = new PositionDto();
        newPosition.setLat(45.56);
        newPosition.setLon(48.23);

        final UserDto newUser = new UserDto();
        newUser.setId("5a0c11c0c98029367ea32955");
        newUser.setLastName("FIRST_");
        newUser.setFirstName("user_");
        newUser.setBirthDay("11/19/2000");
        newUser.setPosition(newPosition);

        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea32955", UserDao.class)).thenReturn(null);
        doNothing().when(this.userMongoDBDaoServices).update("5a0c11c0c98029367ea32955", newUser);

        this.mockMvc.perform(put("/user/5a0c11c0c98029367ea32955").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(newUser))).andExpect(status().isNotFound());
    }

    @Test
    public void testPutOneUserWithInvalidId() throws Exception {
        final PositionDto newPosition = new PositionDto();
        newPosition.setLat(45.56);
        newPosition.setLon(48.23);

        final UserDto newUser = new UserDto();
        newUser.setId("5a0c11c0c98029367ea329e8");
        newUser.setLastName("FIRST_");
        newUser.setFirstName("user_");
        newUser.setBirthDay("11/19/2000");
        newUser.setPosition(newPosition);

        when(this.userMongoDBDaoServices.getOne("fakeId", UserDao.class)).thenThrow(new IllegalArgumentException());
        doNothing().when(this.userMongoDBDaoServices).update("fakeId", newUser);

        this.mockMvc.perform(put("/user/fakeId").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(newUser))).andExpect(status().isNotFound());
    }

    @Test
    public void putAllUsersWithUsers() throws Exception {
        final List<UserDto> users = new ArrayList<UserDto>();

        final PositionDto firstNewPosition = new PositionDto();
        firstNewPosition.setLat(45.56);
        firstNewPosition.setLon(48.23);

        final UserDto firstNewUser = new UserDto();
        firstNewUser.setId("5a0c11c0c98029367ea329e8");
        firstNewUser.setLastName("FIRST");
        firstNewUser.setFirstName("user");
        firstNewUser.setBirthDay("11/19/2000");
        firstNewUser.setPosition(firstNewPosition);

        final PositionDto secondNewPosition = new PositionDto();
        secondNewPosition.setLat(45.56);
        secondNewPosition.setLon(48.23);

        final UserDto secondNewUser = new UserDto();
        secondNewUser.setId("5a0c11c0c98029367b789745");
        secondNewUser.setLastName("SECOND");
        secondNewUser.setFirstName("user");
        secondNewUser.setBirthDay("11/19/2017");
        secondNewUser.setPosition(firstNewPosition);

        users.add(firstNewUser);
        users.add(secondNewUser);

        when(userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(this.users);
        doNothing().when(this.userMongoDBDaoServices).deleteAll();
        doNothing().when(this.userMongoDBDaoServices).insertAll(users.toArray(new UserDto[users.size()]));

        this.mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(users))).andExpect(status().isCreated());
    }

    @Test
    public void putAllUsersWithoutUsers() throws Exception {
        final List<UserDao> users = new ArrayList<UserDao>();
        when(userMongoDBDaoServices.getAllWithLimit(0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(users);
        doNothing().when(this.userMongoDBDaoServices).deleteAll();
        doNothing().when(this.userMongoDBDaoServices).insertAll(new UserDto[0]);

        this.mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(users))).andExpect(status().isCreated());
    }

    @Test
    public void testPostOneUser() throws Exception {
        final PositionDto newPosition = new PositionDto();
        newPosition.setLat(45.2);
        newPosition.setLon(46.2);

        final UserDto newUser = new UserDto();
        newUser.setId("5a0c11c0c98029367ea329e8");
        newUser.setLastName("FIRST");
        newUser.setFirstName("user");
        newUser.setBirthDay("11/19/2000");
        newUser.setPosition(newPosition);

        doNothing().when(this.userMongoDBDaoServices).insert(newUser);
        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(this.firstUser);

        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.jsonSerializer.toJson(newUser))).andExpect(status().isCreated());
    }

    @Test
    public void testSearchByPositiveAge() throws Exception {
        final String today = LocalDate.now().minusYears(15).format(BirthDayDtoSerializer.DATE_PATTERN_OUT);
        final List<UserDao> expectedUsers = new ArrayList<UserDao>();
        expectedUsers.add(this.firstUser);

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(UserController.DEFAULT_AGE_SEARCH_PATTERN, "lt", today), 0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user/age").param("gt", "15")).andExpect(status().isOk()).andExpect(content().string("[{\"id\":\"5a0c11c0c98029367ea329e8\",\"lastName\":\"FIRST\",\"firstName\":\"user\",\"birthDay\":\"11/19/2000\",\"position\":{\"lat\":45.2,\"lon\":46.2}}]"));
    }

    @Test
    public void testSearchByNegativeAge() throws Exception {
        this.mockMvc.perform(get("/user/age").param("gt", "-15")).andExpect(status().isNotFound());
    }

    @Test
    public void testSearchByLastname() throws Exception {
        final List<UserDao> expectedUsers = new ArrayList<UserDao>();
        expectedUsers.add(this.secondUser);

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(UserController.DEFAULT_LAST_NAME_SEARCH_PATTERN, "SECOND"), 0 * UserController.DEFAULT_PAGE, UserController.DEFAULT_PAGE_SIZE, UserController.DEFAULT_SORTING_CONDITION, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user/search").param("term", "SECOND")).andExpect(status().isOk()).andExpect(content().string("[{\"id\":\"5a0c11c0c98029367b789745\",\"lastName\":\"SECOND\",\"firstName\":\"user\",\"birthDay\":\"11/19/2017\",\"position\":{\"lat\":21.24,\"lon\":20.28}}]"));
    }

    @Test
    public void testNearest() throws Exception {
        final List<UserDao> expectedUsers = new ArrayList<UserDao>();
        expectedUsers.add(this.firstUser);

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format(UserController.DEFAULT_LOCATION_SEARCH_PATTERN, "45.2", "46.2"), 0 * UserController.DEFAULT_NEAREST_USERS_PAGE_SIZE, UserController.DEFAULT_NEAREST_USERS_PAGE_SIZE, UserDao.class)).thenReturn(expectedUsers);
        this.mockMvc.perform(get("/user/nearest").param("lon", "45.2").param("lat", "46.2")).andExpect(status().isOk()).andExpect(content().string("[{\"id\":\"5a0c11c0c98029367ea329e8\",\"lastName\":\"FIRST\",\"firstName\":\"user\",\"birthDay\":\"11/19/2000\",\"position\":{\"lat\":45.2,\"lon\":46.2}}]"));
    }
}