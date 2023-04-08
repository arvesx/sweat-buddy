package main.java.activitytracker.server.fileprocessing;

import main.java.activitytracker.Waypoint;

import java.util.HashMap;

public class Chunk {

    private int gpxFileId;
    private int fileId;
    private int chunkId;
    private HashMap<Integer, Waypoint> wps;


    public Chunk(int id, int gpxFileId) {
        this.wps = new HashMap<Integer, Waypoint>();
        this.chunkId = id;
        this.gpxFileId = gpxFileId;
    }

    public void addData(int i, Waypoint wp) {
        this.wps.put(i, wp);
    }

    public void setFileId(int id) {
        this.fileId = id;
    }

    public void setChunkId(int id) {
        this.chunkId = id;
    }

    public int getFileId() {
        return fileId;
    }

    public int getChunkId() {
        return chunkId;
    }


    @Override
    public String toString() {
        String str = "";
        str += "Chunk ID: " + this.chunkId + "\n";
        for (Integer i : this.wps.keySet()) {
            str += "Waypoint " + i + "\n";
            str += "Latitude: " + this.wps.get(i).getLatitude() + "\n";
            str += "Longitude: " + this.wps.get(i).getLongitude() + "\n";
            str += "Elevation: " + this.wps.get(i).getElevation() + "\n";
            str += "Time: " + this.wps.get(i).getTime() + "\n";
            str += "-----------------------------------------------------" + "\n";

        }

        return str;
    }
}
