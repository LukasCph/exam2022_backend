package DTO.BookingDTO;

import DTO.CarDTO.CarDTO;
import DTO.WashingAssistantDTO.WashingAssistantDTO;
import entities.Booking;
import entities.Car;
import entities.WashingAssistant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDTO {

    private Date bookingDate;
    private int duration;
    private List<WashingAssistantDTO> washers = new ArrayList<>();
    private CarDTO car;

    public BookingDTO(Booking booking) {
        this.bookingDate = booking.getBookingDate();
        this.duration = booking.getDuration();
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

    public List<WashingAssistantDTO> getWashers() {
        return washers;
    }

    public void setWashers(List<WashingAssistantDTO> washers) {
        this.washers = washers;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }
}
