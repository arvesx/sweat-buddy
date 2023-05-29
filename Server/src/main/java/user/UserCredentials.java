package user;

public class UserCredentials {
    public int userId;
    public String username;
    public String hashedPassword;

    public UserCredentials(int userId, String username, String hashedPassword) {
        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }
}
