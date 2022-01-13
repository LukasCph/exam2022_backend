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


    public BookingDTO createBooking(BookingDTO bookingDTO, CarDTO carDTO, String name) {
        EntityManager em = emf.createEntityManager();

        Booking booking = null;
        Car car = null;

        try {
            em.getTransaction().begin();
            car  = new Car(carDTO.getRegistration(),car.getBrand(),car.getMake(),car.getYear());
            booking = new Booking(bookingDTO.getBookingDate(), booking.getDuration());
            car.addBookings(booking);

            booking.addWasher(
                    em.find(WashingAssistant.class, name)
            );
            em.persist(car);
            em.persist(booking);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new BookingDTO(booking);
    }
}
