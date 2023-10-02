package vn.edu.usth.gmail;

public class User {
    private String email;
    private String id;
    private int image;

    public User(String email, String id) {
        this.email = email;
        this.id = id;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
