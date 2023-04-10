package main.java.activitytracker.fileprocessing;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

import static main.java.activitytracker.server.Utilities.CHUNK_SIZE;

public class GpxFile implements Serializable {

    private int gpxFileId;
    private ArrayList<main.java.activitytracker.Waypoint> wps;


    private final ArrayList<Chunk> chunks;

    public GpxFile(String file_name) {
        this.initVariables();
        this.initGpxObject(file_name);
        this.chunks = new ArrayList<>();
    }

    private void initVariables() {
        this.wps = new ArrayList<>();
    }


    public void makeChunks() {

        int chunkId = 0;
        for (int i = 0; i < wps.size(); i += CHUNK_SIZE) {

            Chunk chunk = new Chunk(chunkId, this.gpxFileId);
            for (int j = 0; j < CHUNK_SIZE; j++) {
                if (i + j > wps.size() - 1) {
                    break;
                }
                chunk.addData(this.wps.get(i + j));
            }
            this.chunks.add(chunk);
            chunkId++;
        }
    }

    private void initGpxObject(String file_name) {
        FileInputStream in = null;

        GPXParser p = new GPXParser();
        GPX gpx = null;

        try {
            in = new FileInputStream(file_name);
        } catch (Exception e) {
            System.err.println("ERROR: File_Input");
        }

        try {
            gpx = p.parseGPX(in);
        } catch (Exception e) {
            System.err.println("ERROR: GPX_Parse");
        }

        ArrayList<main.java.activitytracker.Waypoint> temporaryList = new ArrayList<>();

        int i = 0;
        assert gpx != null;
        for (Waypoint wp : gpx.getWaypoints()) {

            temporaryList.add(new main.java.activitytracker.Waypoint(wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

            ++i;
        }

        temporaryList.sort(new WaypointTimeComparator());
        int id = 0;
        for (var wp : temporaryList) {
            wp.setID(id);
            id++;
        }

        this.wps = temporaryList;
    }

    public ArrayList<Chunk> getChunks() {
        return this.chunks;
    }

    public ArrayList<main.java.activitytracker.Waypoint> getWps() {
        return this.wps;
    }

    public int getGpxFileId() {
        return gpxFileId;
    }

    public void setGpxFileId(int gpxFileId) {
        this.gpxFileId = gpxFileId;
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();
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
