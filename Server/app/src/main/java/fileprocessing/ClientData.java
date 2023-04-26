package fileprocessing;

import dependencies.fileprocessing.gpx.GpxFile;

public class ClientData {
    
    private int ID;
    private String username;
    private GpxFile gpxFile;

    public ClientData(int id, String username, GpxFile gpxFile)
    {
        this.ID = id;
        this.username = username;
        this.gpxFile = gpxFile;
    }

    public ClientData(int id)
    {
        this.ID = id;
        this.username = "";
        this.gpxFile = null;
    }

    public ClientData()
    {
        this.ID = -1;
        this.username = "";
        this.gpxFile = null;
    }

    public int getID() {
        return this.ID;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GpxFile getGpxFile() {
        return this.gpxFile;
    }

    public void setGpxFile(GpxFile gpx_file) {
        this.gpxFile = gpx_file;
    }

    public void setID(int id) {
        this.ID = id;
    }

    @Override
    public String toString()
    {
        return "{ID: " + this.ID +
             ", Username: " + this.username +
              ", GPXFile: " + this.gpxFile + "}";
    }
    

}
