package dependencies.fileprocessing.gpx;

import java.io.Serializable;
import java.util.ArrayList;

public class Chunk implements Serializable {

    private final int gpxFileId;
    private int fileId;
    private int chunkId;
    private final ArrayList<WaypointImpl> wps;

    public Chunk(int id, int gpxFileId) {
        this.wps = new ArrayList<WaypointImpl>();
        this.chunkId = id;
        this.gpxFileId = gpxFileId;
    }

    public void addData(WaypointImpl wp) {
        this.wps.add(wp);
    }

    public ArrayList<WaypointImpl> getWaypoints() {
        return wps;
    }

    public int getGpxFileId() {
        return gpxFileId;
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
        for (WaypointImpl wp : this.wps) {
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
