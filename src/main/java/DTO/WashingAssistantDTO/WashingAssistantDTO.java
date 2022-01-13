package DTO.WashingAssistantDTO;

import DTO.BookingDTO.BookingDTO;
import DTO.CarDTO.CarDTO;
import entities.Booking;
import entities.Car;
import entities.WashingAssistant;

import java.util.ArrayList;
import java.util.List;

public class WashingAssistantDTO {

    private String washerName;
    private String language;
    private int yearsOfExp;;
    private int priceHour;
    private List<BookingDTO> bookings = new ArrayList<>();

    public WashingAssistantDTO(WashingAssistant washingAssistant) {
        this.washerName = washingAssistant.getWasherName();
        this.language = washingAssistant.getLanguage();
        this.yearsOfExp = washingAssistant.getYearsOfExp();
        this.priceHour = washingAssistant.getPriceHour();
    }

    public WashingAssistantDTO() {
    }

    public static List<WashingAssistantDTO> getDTO(List<WashingAssistant> washingAssistants){
        if (washingAssistants != null) {
            List <WashingAssistantDTO> washingAssistantsDTO = new ArrayList<>();
            washingAssistants.forEach(w -> washingAssistantsDTO.add(new WashingAssistantDTO(w)));
            return washingAssistantsDTO;
        } else
            return null;
    }

    public String getWasherName() {
        return washerName;
    }

    public void setWasherName(String washerName) {
        this.washerName = washerName;
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

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }
}
