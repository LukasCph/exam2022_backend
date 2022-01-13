package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.awt.print.Book;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SetupTestUsers {

  public static void main(String[] args) {
    setupTestUsers();
  }

  public static void setupTestUsers(){
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();

    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    Calendar cal = Calendar.getInstance();
    cal.set(2022,01,12);
    cal.setTimeZone(TimeZone.getTimeZone("CET"));

    User user = new User("user", "kode123");
    User admin = new User("admin", "kode123");
    User both = new User("user_admin", "kode123");
    Car bil1 = new Car("AE23513","Fiat","Punto","1996");
    cal.set(2022,01,20,12,30);
    Booking book1 = new Booking("25/01/2022",30);
    WashingAssistant wash1 = new WashingAssistant("Morten","Dansk",4,50);


    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);

    bil1.addBookings(book1);
    book1.addWasher(wash1);
    wash1.addBookings(book1);
    user.setCar(bil1);

    em.persist(bil1);
    em.persist(book1);
    em.persist(wash1);

    em.persist(userRole);
    em.persist(adminRole);
    em.persist(user);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    //System.out.println("PW: " + user.getUserPass());
    //System.out.println("Testing user with OK password: " + user.verifyPassword("Kodener123",user.getUserPass()));
    //System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    //System.out.println("Created TEST Users");
  }

}