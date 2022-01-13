package facades;

import DTO.BookingDTO.BookingDTO;
import DTO.CarDTO.CarDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import entities.Booking;
import entities.Car;
import entities.WashingAssistant;

import javax.enterprise.inject.Typed;
import javax.persistence.*;
import javax.ws.rs.NotFoundException;
import java.lang.annotation.Native;
import java.util.List;
import java.util.Queue;


public class CarFacade {

        private static facades.CarFacade instance;
        private static EntityManagerFactory emf;
        private Gson gson = new Gson();

        //Private Constructor to ensure Singleton
        private CarFacade() {}


        public static facades.CarFacade getCarFacade(EntityManagerFactory _emf) {
            if (instance == null) {
                emf = _emf;
                instance = new facades.CarFacade();
            }
            return instance;
        }


    public String addCarOntoBooking(int id,String registration, String brand, String make, String year) {
        EntityManager em = emf.createEntityManager();
        Booking bookingToEdit  = null;
        Car carToAdd = null;

        carToAdd = new Car(registration,brand,make,year);
        bookingToEdit = em.find(Booking.class,id);

        carToAdd.addBookings(bookingToEdit);

        try {
            em.getTransaction().begin();
            em.persist(carToAdd);
            em.merge(bookingToEdit);
            em.persist(bookingToEdit);
            em.getTransaction().commit();
        } catch (Exception e){
            throw new NotFoundException("Persist failed");
        } finally {
            em.close();
        }
        System.out.println(bookingToEdit.getWashers());
        return "Assistant: "+carToAdd+" has been added to the booking";
    }
}
