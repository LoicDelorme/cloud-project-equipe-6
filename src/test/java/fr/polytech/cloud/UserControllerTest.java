package fr.polytech.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.dao.PositionDao;
import fr.polytech.cloud.entities.dao.UserDao;
import fr.polytech.cloud.entities.dto.PositionDto;
import fr.polytech.cloud.entities.dto.UserDto;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.apache.catalina.User;
import org.junit.Before;
import org.junit.Ignore;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private ObjectMapper objectMapper;

    private final PositionDao position = new PositionDao();
    private final PositionDao position2 = new PositionDao();

    private final UserDao user1 = new UserDao();
    private final UserDao user2 = new UserDao();

    final ArrayList<UserDao> userList = new ArrayList();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.objectMapper = new ObjectMapper();

        position.setType("Point");
        position.setCoordinates(Arrays.asList(45.2, 46.2));

        position2.setType("Point");
        position2.setCoordinates(Arrays.asList(20.28, 21.24));

        user1.setId("5a0c11c0c98029367ea329e8");
        user1.setLastName("TEST");
        user1.setFirstName("test");
        user1.setBirthDay("11/19/2000");
        user1.setPosition(position);

        user2.setId("5a0c11c0c98029367b789745");
        user2.setLastName("LOL");
        user2.setFirstName("lol");
        user2.setBirthDay("11/19/2017");
        user2.setPosition(position2);

        userList.add(user1);
        userList.add(user2);
    }

    @Test
    public void testGetAllUsersWithoutUsers() throws Exception {
        final ArrayList<UserDao> expectedUserList = new ArrayList();

        when(this.userMongoDBDaoServices.getAll(UserDao.class)).thenReturn(new ArrayList<UserDao>());
        this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(content().string(expectedUserList.toString()));
    }

    @Test
    public void testGetAllUsersWithUsers() throws Exception {

        String expected = "[";

        for (UserDao user : userList) {
            expected += this.objectMapper.writeValueAsString(user);
            if(userList.get(userList.size()-1) != user){
                expected += ",";
            }
        }
        expected += "]";

        when(this.userMongoDBDaoServices.getAllWithLimit(0,100, "{lastName: -1}",UserDao.class)).thenReturn(userList);
        this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(content().string(expected));
    }

    @Test
    public void testGetOneUserWithCorrectId() throws Exception {

        final String expected = this.objectMapper.writeValueAsString(user1);

        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(user1);
        this.mockMvc.perform(get("/user/5a0c11c0c98029367ea329e8")).andExpect(status().isOk()).andExpect(content().string(expected));
    }

    @Test
    public void testGetOneUserWithUnknownId() throws Exception {

        final String expected = "Unknown user for the provided ID!";

        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(user1);
        this.mockMvc.perform(get("/user/5a0c11c0c98029367ea32955")).andExpect(status().isNotFound()).andExpect(content().string(expected));
    }

    //To Fix : Invalid ID

    @Test
    public void testDeleteAllUsersWithUsers() throws Exception {

        when(userMongoDBDaoServices.getAllWithLimit(0,100, "{lastName: -1}",UserDao.class)).thenReturn(this.userList);
        doNothing().when(userMongoDBDaoServices).deleteAll();

        mockMvc.perform(delete("/user")).andExpect(status().isOk());
    }

    @Test
    public void testDeleteAllUsersWithoutUsers() throws Exception {

        when(userMongoDBDaoServices.getAllWithLimit(0,100, "{lastName: -1}",UserDao.class)).thenReturn(new ArrayList<>());
        doNothing().when(userMongoDBDaoServices).deleteAll();

        mockMvc.perform(delete("/user")).andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserFound() throws Exception {

        when(userMongoDBDaoServices.getOne(user1.getId(), UserDao.class)).thenReturn(user1);
        doNothing().when(userMongoDBDaoServices).delete(user1.getId());

        mockMvc.perform(delete("/user/5a0c11c0c98029367ea329e8")).andExpect(status().is(204));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {

        when(userMongoDBDaoServices.getOne(user1.getId(), UserDao.class)).thenReturn(user1);
        doNothing().when(userMongoDBDaoServices).delete(user1.getId());

        mockMvc.perform(delete("/user/5a0c11c0c98029367ea34444")).andExpect(status().is(500));
    }

    @Test
    public void testUpdateUserFound() throws Exception {

        PositionDto posDto = new PositionDto();
        posDto.setLat(45.56);
        posDto.setLon(48.23);

        UserDto userDto = new UserDto();
        userDto.setId(user1.getId());
        userDto.setBirthDay(user1.getBirthDay());
        userDto.setFirstName(user2.getFirstName());
        userDto.setLastName(user2.getLastName());
        userDto.setPosition(posDto);

        when(userMongoDBDaoServices.getOne(user1.getId(), UserDao.class)).thenReturn(user1);
        doNothing().when(userMongoDBDaoServices).update(user1.getId(), userDto);

        mockMvc.perform(
                put("/user/5a0c11c0c98029367ea329e8", userDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserNotFoundUnknownId() throws Exception {

        PositionDto posDto = new PositionDto();
        posDto.setLat(45.56);
        posDto.setLon(48.23);

        UserDto userDto = new UserDto();
        userDto.setId(user1.getId());
        userDto.setBirthDay(user1.getBirthDay());
        userDto.setFirstName(user2.getFirstName());
        userDto.setLastName(user2.getLastName());
        userDto.setPosition(posDto);

        String expected = "Unknown user for the provided ID!";

        when(userMongoDBDaoServices.getOne(user1.getId(), UserDao.class)).thenReturn(user1);
        doNothing().when(userMongoDBDaoServices).update("5a0c11c0c98029367ea34444", userDto);

        mockMvc.perform(
                put("/user/5a0c11c0c98029367ea34444", userDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(user1)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expected));
    }

    //To fix : Invalid Id

    @Test
    public void testUpdateListUsersWithUsers() throws Exception {

        ArrayList<UserDto> userListDto = new ArrayList<>();

        PositionDto posDto = new PositionDto();
        posDto.setLat(45.56);
        posDto.setLon(48.23);

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId());
        userDto1.setBirthDay(user1.getBirthDay());
        userDto1.setFirstName(user2.getFirstName());
        userDto1.setLastName(user2.getLastName());
        userDto1.setPosition(posDto);

        UserDto userDto2 = new UserDto();
        userDto1.setId(user2.getId());
        userDto1.setBirthDay(user2.getBirthDay());
        userDto1.setFirstName(user1.getFirstName());
        userDto1.setLastName(user1.getLastName());
        userDto1.setPosition(posDto);

        userListDto.add(userDto1);
        userListDto.add(userDto2);

        String dataString = "[";

        for (UserDto user : userListDto) {
            dataString += this.objectMapper.writeValueAsString(user);
            if(userListDto.get(userListDto.size()-1) != user){
                dataString += ",";
            }
        }
        dataString += "]";

        when(userMongoDBDaoServices.getAllWithLimit(0,100, "{lastName: -1}",UserDao.class)).thenReturn(this.userList);
        doNothing().when(userMongoDBDaoServices).deleteAll();
        doNothing().when(userMongoDBDaoServices).insertAll(userListDto.toArray(new UserDto[userListDto.size()]));

        mockMvc.perform(
                put("/user", userListDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataString))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateListUsersWithoutUsers() throws Exception {

        ArrayList<UserDto> userListDto = new ArrayList<>();

        PositionDto posDto = new PositionDto();
        posDto.setLat(45.56);
        posDto.setLon(48.23);

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId());
        userDto1.setBirthDay(user1.getBirthDay());
        userDto1.setFirstName(user2.getFirstName());
        userDto1.setLastName(user2.getLastName());
        userDto1.setPosition(posDto);

        UserDto userDto2 = new UserDto();
        userDto1.setId(user2.getId());
        userDto1.setBirthDay(user2.getBirthDay());
        userDto1.setFirstName(user1.getFirstName());
        userDto1.setLastName(user1.getLastName());
        userDto1.setPosition(posDto);

        userListDto.add(userDto1);
        userListDto.add(userDto2);

        String dataString = "[";

        for (UserDto user : userListDto) {
            dataString += this.objectMapper.writeValueAsString(user);
            if(userListDto.get(userListDto.size()-1) != user){
                dataString += ",";
            }
        }
        dataString += "]";

        when(userMongoDBDaoServices.getAllWithLimit(0,100, "{lastName: -1}",UserDao.class)).thenReturn(new ArrayList<>());
        doNothing().when(userMongoDBDaoServices).deleteAll();
        doNothing().when(userMongoDBDaoServices).insertAll(userListDto.toArray(new UserDto[userListDto.size()]));

        mockMvc.perform(
                put("/user", userListDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataString))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUser() throws Exception {

        PositionDto posDto = new PositionDto();
        posDto.setLat(45.56);
        posDto.setLon(48.23);

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId());
        userDto1.setBirthDay(user1.getBirthDay());
        userDto1.setFirstName(user2.getFirstName());
        userDto1.setLastName(user2.getLastName());
        userDto1.setPosition(posDto);

        //To Do : Check if user already exists
        doNothing().when(userMongoDBDaoServices).insert(userDto1);

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto1)))
                .andExpect(status().isCreated());
    }

    @Test
    @Ignore
    // To Fix : Parameters
    public void testSearchByAgeValidAge() throws Exception {

        String expected = "["+ this.objectMapper.writeValueAsString(user1) + "]";

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format("{birthDay: {$%s: '%s'}}", "gt", "20020101") ,0,100, "{lastName: -1}",UserDao.class)).thenReturn(userList);
        this.mockMvc.perform(
                get("/user/age")
                .param("gt", "15"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    public void testSearchByLastName() throws Exception {

        String expected = "["+ this.objectMapper.writeValueAsString(user2) + "]";
        ArrayList<UserDao> testList = new ArrayList<>();
        testList.add(user2);

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format("{lastName: '%s'}", "LOL"), 0,100, "{lastName: -1}",UserDao.class)).thenReturn(testList);
        this.mockMvc.perform(
                get("/user/search")
                        .param("term", "LOL"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    @Ignore
    // To Fix : Empty return
    public void testSearchNearest() throws Exception {

        String expected = "["+ this.objectMapper.writeValueAsString(user2) + "]";

        ArrayList<UserDao> testList = new ArrayList<>();
        testList.add(user2);

        when(this.userMongoDBDaoServices.getAllWhereWithLimit(String.format("{position: {$near: {$geometry: {type: 'Point' , coordinates: [%s,%s]}}}}", "20.28", "21.24"), 0,100, "{lastName: -1}",UserDao.class)).thenReturn(testList);
        this.mockMvc.perform(
                get("/user/nearest")
                        .param("lon", "20.28")
                        .param("lat", "21.24"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}