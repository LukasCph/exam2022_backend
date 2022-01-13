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
import java.lang.annotation.Native;
import java.util.List;
import java.util.Queue;

public class BookingFacade {

    private static BookingFacade instance;
    private static EntityManagerFactory emf;
    private Gson gson = new Gson();

    //Private Constructor to ensure Singleton
    private BookingFacade() {}


    public static BookingFacade getBookingFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookingFacade();
        }
        return instance;
    }


    public BookingDTO createBooking(BookingDTO bookingDTO) {
        EntityManager em = emf.createEntityManager();

        Booking booking = null;
        try {
            em.getTransaction().begin();
            booking = new Booking(bookingDTO.getBookingDate(), bookingDTO.getDuration());
            em.persist(booking);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new BookingDTO(booking);
    }



}
