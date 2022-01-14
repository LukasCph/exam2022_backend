package rest;

import DTO.BookingDTO.BookingDTO;
import DTO.CarDTO.CarDTO;
import DTO.WashingAssistantDTO.WashingAssistantDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import facades.AssistantFacade;
import facades.BookingFacade;
import facades.CarFacade;
import utils.EMF_Creator;

@Path("cw")
public class CarWashResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AssistantFacade ASSISTANTFACADE = AssistantFacade.getAssistantFacade(EMF);
    private static final BookingFacade BOOKINGFACADE = BookingFacade.getBookingFacade(EMF);
    private static final CarFacade CARFACADE = CarFacade.getCarFacade(EMF);

    @Context
    private UriInfo context;
    private Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//    private Gson gson = new Gson();

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allAssistants")
    public String fetchAllAssistants(){
        List<WashingAssistantDTO> assistantDTO = null;
        try {
            assistantDTO = ASSISTANTFACADE.getAllAssistants();
        } catch (Exception e) {
            throw new NotFoundException("Error fetching all assistants");
        }
        if (assistantDTO != null & !assistantDTO.isEmpty()) {
            return GSON.toJson(assistantDTO);
        } else {
            throw new NotFoundException("Error fetching all assistants");
        }
    }

    //DOES NOT WORK AS INTENDED - MIGHT BE A WRONG RELATION - MIGHT NEED TO CALL FROM USER INSTEAD
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mycar/{username}")

    public String fetchMyCar(@PathParam("username")String username){
        CarDTO carDTO = null;
        try {
            carDTO = CARFACADE.getCar(username);
        } catch (Exception e) {
            throw new NotFoundException("Error finding your car");
        }
        if (carDTO != null ) {
            return GSON.toJson(carDTO);
        } else {
            throw new NotFoundException("Error fetching all assistants");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("book")
    public String makeNewBooking(BookingDTO bookingDTO) {
        BookingDTO bookingResponseDTO = null;

        if (bookingDTO != null) {
            try {
                bookingResponseDTO = BOOKINGFACADE.createBooking(bookingDTO);
            } catch (Exception e) {
                throw new NotFoundException("Booking failed");
            }
        } else {
            throw new NotFoundException("Missing booking info");
        }

        if (bookingResponseDTO != null) {
            return GSON.toJson(bookingResponseDTO);
        }
        throw new NotFoundException("Booking could not be made");
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("booking/{id}")
    public String addAssistantToBooking(@PathParam("id") int id, String jsonString) {

        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        String washerName = json.get("washerName").getAsString();
        System.out.println(washerName);
        System.out.println(id);

        if (id != 0) {
            try {
                ASSISTANTFACADE.addAssistant(id,washerName);
            } catch (Exception e) {
                throw new NotFoundException("Missing booking info");
            }
        } else {
            throw new NotFoundException("Failed to add assistant");
        }
        return "Assistant: "+ washerName +" has been added to the booking";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("newassistant")
    public String createAssistant(WashingAssistantDTO washingAssistantDTO) {
        WashingAssistantDTO assistantResponseDTO = null;

        if (washingAssistantDTO != null) {
            try {
                assistantResponseDTO = ASSISTANTFACADE.newAssistant(washingAssistantDTO);
            } catch (Exception e) {
                throw new NotFoundException("Assistant could not be created");
            }
        } else {
            throw new NotFoundException("Assistant missing");
        }
        if (assistantResponseDTO != null){
            return GSON.toJson(assistantResponseDTO);
        } else {
            throw new NotFoundException("Assistant could not be created");
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("removeassistant/{name}")
    public String removeAssistant(@PathParam("name") String assistantName){
        WashingAssistantDTO assistantResponseDTO = null;

        if (assistantName != null ){
            try {
                assistantResponseDTO = ASSISTANTFACADE.removeWashingAssistant(assistantName);
            } catch (Exception e) {
                throw new NotFoundException("Assistant could not be removed");
            }
        } else {
                throw new NotFoundException("Assistant not found");
               }
            if (assistantResponseDTO != null ) {
                return GSON.toJson(assistantResponseDTO);
            } else {
                throw new NotFoundException("Assistant could not be removed");
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addcar/{id}")
    public String addCarToBooking(@PathParam("id") int id, String jsonString) {

        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        String registration = json.get("registration").getAsString();
        String brand = json.get("brand").getAsString();
        String make = json.get("make").getAsString();
        String year = json.get("year").getAsString();

        if (id != 0) {
            try {
                CARFACADE.addCarOntoBooking(id, registration, brand, make, year);
            } catch (Exception e) {
                throw new NotFoundException("id is not correct");
            }
        } else {
            throw new NotFoundException("Failed to add car");
        }
        return "Car with registration Id: "+ registration +" has been added to the booking";
    }
}