package rest;

import DTO.BookingDTO.BookingDTO;
import DTO.CarDTO.CarDTO;
import DTO.WashingAssistantDTO.WashingAssistantDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;

import java.util.List;
import java.util.concurrent.ExecutionException;
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
import utils.EMF_Creator;

@Path("cw")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AssistantFacade ASSISTANTFACADE = AssistantFacade.getAssistantFacade(EMF);
    private static final BookingFacade BOOKINGFACADE = BookingFacade.getBookingFacade(EMF);

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

/*
    DOES NOT WORK
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("book/{name}")
    public String makeNewBooking(@PathParam("name") String name, BookingDTO bookingDTO, CarDTO carDTO) {


        BookingDTO bookingResponseDTO = null;

        if (bookingDTO != null) {
            try {
                bookingResponseDTO = BOOKINGFACADE.createBooking(bookingDTO, carDTO, name);
            } catch (Exception e) {
                throw new NotFoundException("Booking could not be made");
            }
        } else {
            throw new NotFoundException("Missing booking info");
        }

        if (bookingResponseDTO != null) {
            return GSON.toJson(bookingResponseDTO);
        }
        throw new NotFoundException("Booking could not be made");
    }
    /*
 */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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


}