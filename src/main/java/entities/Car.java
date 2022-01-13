package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @Column (name = "registration")
    @NotNull
    private String registration;
    @Column (name = "brand")
    private String brand;
    @Column (name = "make")
    private String make;
    @Column (name = "year")
    private String year;

    @OneToOne(mappedBy = "car")
    private User user;

    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST})
    private List<Booking> bookings = new ArrayList<>();

    public Car(String registration, String brand, String make, String year) {
        this.registration = registration;
        this.brand = brand;
        this.make = make;
        this.year = year;
        this.bookings = bookings;
        this.user = user;
    }

    public Car() {
    }

    public void addBookings(Booking booking) {
        this.bookings.add(booking);
        if (booking != null) {
            booking.setCar(this);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
