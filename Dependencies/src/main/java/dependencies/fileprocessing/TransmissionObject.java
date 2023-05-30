package dependencies.fileprocessing;

import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.user.UserData;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;
    public UserData userData;
    public GpxResults gpxResults;

    public String username;
    public String password;

    public String message;
    public int success;
}
