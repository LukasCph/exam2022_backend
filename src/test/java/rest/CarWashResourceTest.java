package rest;

import com.nimbusds.jose.shaded.json.JSONObject;
import entities.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.*;
import utils.EMF_Creator;

@Disabled
public class CarWashResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;


    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
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

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/cw").then().statusCode(200);
    }

    @Test
    public void testBookCarWash() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("bookingDate","24-11-2022");
        requestParams.put("duration",60);

        login("user","test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(requestParams.toJSONString())
              .when().
                post("/cw/book")
              .then().statusCode(200)
                .body("bookingDate",equalTo("24-11-2022"));
    }

    @Test
    public void userCannotCreateAssistant() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("washerName","Minnie");
        requestParams.put("language","French");
        requestParams.put("yearsOfExp",23);
        requestParams.put("priceHour",120);

        login("user","test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(requestParams.toJSONString())
                .when().
                post("/cw/newassistant")
                .then().statusCode(401);
    }

    @Test
    public void adminCanCreateAssistant() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("washerName","Minnie");
        requestParams.put("language","French");
        requestParams.put("yearsOfExp",23);
        requestParams.put("priceHour",120);

        login("admin","test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(requestParams.toJSONString())
                .when().
                post("/cw/newassistant")
                .then().statusCode(200).body("washerName",equalTo("Minnie"));
    }

    @Test
    public void adminCanRemoveAssistant() {
        JSONObject requestParams = new JSONObject();
        login("admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when().
                delete("/cw/removeassistant/Karsten")
                .then().statusCode(200);
    }
}

