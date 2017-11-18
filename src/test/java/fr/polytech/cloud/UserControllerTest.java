package fr.polytech.cloud;

import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.dao.PositionDao;
import fr.polytech.cloud.entities.dao.UserDao;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class UserControllerTest {

    @Mock
    private UserMongoDBDaoServices userMongoDBDaoServices;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsersWithoutUsers() throws Exception {
        when(this.userMongoDBDaoServices.getAll(UserDao.class)).thenReturn(new ArrayList<UserDao>());
        this.mockMvc.perform(get("/user")).andExpect(status().isOk()).andExpect(content().string("[]"));
    }

    @Test
    public void testGetOneUser() throws Exception {
        final PositionDao position = new PositionDao();
        position.setType("Point");
        position.setCoordinates(Arrays.asList(45.2, 46.2));

        final UserDao user = new UserDao();
        user.setId("5a0c11c0c98029367ea329e8");
        user.setLastName("TEST");
        user.setFirstName("test");
        user.setBirthDay("12/25/2017");
        user.setPosition(position);

        when(this.userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(user);
        this.mockMvc.perform(get("/user/5a0c11c0c98029367ea329e8")).andExpect(status().isOk()).andExpect(content().string("{\"id\":\"5a0c11c0c98029367ea329e8\",\"lastName\":\"TEST\",\"firstName\":\"test\",\"birthDay\":\"12/25/2017\",\"position\":{\"lat\":46.2,\"lon\":45.2}}"));
    }
}