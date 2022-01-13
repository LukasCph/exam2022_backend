package DTO.CarDTO;

import DTO.BookingDTO.BookingDTO;
import entities.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDTO {

    private String registration;
    private String brand;
    private String make;
    private String year;
    private List<BookingDTO> bookings = new ArrayList<>();


    public CarDTO(Car car) {
        this.registration = car.getRegistration();
        this.brand = car.getBrand();
        this.make = car.getMake();
        this.year = car.getYear();
    }

    public static List<CarDTO> getDTO(List<Car> cars){
        if (cars != null) {
            List <CarDTO> carsDTO = new ArrayList<>();
            cars.forEach(c -> carsDTO.add(new CarDTO(c)));
            return carsDTO;
        } else
            return null;
    }

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
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
}
