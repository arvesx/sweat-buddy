package main.java.activitytracker.server.fileprocessing;

import main.java.activitytracker.Waypoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Chunk implements Serializable {

    private final int gpxFileId;
    private int fileId;
    private int chunkId;
    private HashMap<Integer, Waypoint> wps;


    public Chunk(int id, int gpxFileId) {
        this.wps = new ArrayList<Waypoint>();
        this.chunkId = id;
        this.gpxFileId = gpxFileId;
    }

    public void addData(Waypoint wp) {
        this.wps.add(wp);
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

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Chunk ID: ").append(this.chunkId).append("\n\n");
        for (main.java.activitytracker.Waypoint wp : this.wps) {
            strBuilder.append("Waypoint ").append(wp.getID()).append("\n")
                    .append("Latitude: ").append(wp.getLatitude()).append("\n")
                    .append("Longitude: ").append(wp.getLongitude()).append("\n")
                    .append("Elevation: ").append(wp.getElevation()).append("\n")
                    .append("Time: ").append(wp.getTime()).append("\n")
                    .append("-----------------------------------------------------" + "\n");
        }

        return strBuilder.toString();
    }
}
