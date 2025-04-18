package src.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String address;

    public User(String name, String email, String username, String password, String address) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }

    // Optionally, add Setters if you want to edit fields later
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
}
