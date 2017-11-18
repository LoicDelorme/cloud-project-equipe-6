package fr.polytech.cloud;

import fr.polytech.cloud.controllers.UserController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testGetUserWithoutParameter() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserWithId() throws Exception {
        when(userMongoDBDaoServices.getOne("5a0c11c0c98029367ea329e8", UserDao.class)).thenReturn(new UserDao());
        mockMvc.perform(get("/user/5a0c11c0c98029367ea329e8")).andExpect(status().isOk());
    }
}