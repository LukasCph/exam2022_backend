package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assistants")
public class WashingAssistant {

    @Id
    @Column(name = "washerName")
    private String washerName;
    @Column(name = "language")
    private String language;
    @Column(name = "years_of_exp")
    private int yearsOfExp;
    @Column(name = "price_hour")
    private int priceHour;

    /*@JoinTable(name = "booking_washers", joinColumns = {
            @JoinColumn(name = "bookingId", referencedColumnName = "bookingId")}, inverseJoinColumns = {
            @JoinColumn(name = "washerName", referencedColumnName = "washerName")}*/

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Booking> bookings = new ArrayList<>();

    public WashingAssistant() {
    }

    public WashingAssistant(String washerName, String language, int yearsOfExp, int priceHour) {
        this.washerName = washerName;
        this.language = language;
        this.yearsOfExp = yearsOfExp;
        this.priceHour = priceHour;
    }

    public void addBookings(Booking booking) {
        this.bookings.add(booking);
    }

    public String getWasherName() {
        return washerName;
    }

    public void setWasherName(String name) {
        this.washerName = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public int getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(int priceHour) {
        this.priceHour = priceHour;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
