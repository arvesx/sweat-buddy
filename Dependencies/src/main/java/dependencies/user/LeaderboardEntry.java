package dependencies.user;

public class LeaderboardEntry {
    
    public int userId;
    public String name;
    public int points;

    public LeaderboardEntry(int userId, String name, int points) {
        this.userId = userId;
        this.name = name;
        this.points = points;
    }
}
