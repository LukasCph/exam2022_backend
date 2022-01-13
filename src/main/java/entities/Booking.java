package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;
    private int duration;


    @ManyToMany(mappedBy = "bookings", cascade = {CascadeType.PERSIST})
    private List<WashingAssistant> washers = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Car car;

    public Booking(Date bookingDate, int duration) {
        this.bookingDate = bookingDate;
        this.duration = duration;
    }

    public Booking() {
    }

    public void addWasher(WashingAssistant washingAssistant) {
        this.washers.add(washingAssistant);
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<WashingAssistant> getWashers() {
        return washers;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
