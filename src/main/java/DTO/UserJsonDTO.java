package DTO;

public class UserJsonDTO {

    private int id;
    private String name;
    private String email;
    private String username;
    private String phone;

    public UserJsonDTO(int id, String name, String email, String username, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
    }
}
