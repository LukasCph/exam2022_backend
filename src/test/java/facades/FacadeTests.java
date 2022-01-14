package facades;

import DTO.WashingAssistantDTO.WashingAssistantDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONObject;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Disabled
public class FacadeTests {

    private static EntityManagerFactory emf;
    private static AssistantFacade ASSISTANTFACADE;
    private Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        ASSISTANTFACADE = AssistantFacade.getAssistantFacade(emf);
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Booking ").executeUpdate();
            em.createQuery("delete from Car").executeUpdate();
            em.createQuery("delete from WashingAssistant ").executeUpdate();
            em.getTransaction().commit();

            em.getTransaction().begin();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);

            Car car1 = new Car("AA35000","Mercedes","Benz","2000");
            Booking booking1 = new Booking("24-12-2022",30);
            WashingAssistant assistant1 = new WashingAssistant("Karsten","Dansk",6,120);
            user.setCar(car1);
            booking1.setCar(car1);
            booking1.addWasher(assistant1);

            em.persist(booking1);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @DisplayName("Test strict = true")
    @Test
    public void testCreateAssistant() {
        EntityManager em = emf.createEntityManager();
        try {
            WashingAssistantDTO testDTO;

            em.getTransaction().begin();
            JSONObject testJson = new JSONObject();
            testJson.put("washerName","Kenny");
            testJson.put("language","Dansk");
            testJson.put("yearsOfExp",4);
            testJson.put("priceHour",520);
            System.out.println(testJson);

            testDTO = GSON.fromJson(testJson.toJSONString(), WashingAssistantDTO.class);
            WashingAssistantDTO returnedDTO = ASSISTANTFACADE.newAssistant(testDTO);
            Assertions.assertEquals(WashingAssistantDTO.class,returnedDTO.getClass());

        } finally {
            em.close();
        }
    }
}
