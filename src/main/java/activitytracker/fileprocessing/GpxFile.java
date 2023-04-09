package main.java.activitytracker.server.fileprocessing;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

import static main.java.activitytracker.server.Utilities.CHUNK_SIZE;

public class GpxFile implements Serializable {

    private int gpxFileId;
    private ArrayList<main.java.activitytracker.Waypoint> wps;


    private ArrayList<Chunk> chunks;

    public GpxFile(String file_name) {
        this.initVariables();
        this.initGpxObject(file_name);
        this.chunks = new ArrayList<Chunk>();
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

        int i = 0;
        for (Waypoint wp : gpx.getWaypoints()) {

            this.wps.add(i, new main.java.activitytracker.Waypoint(i, wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

            ++i;
        }
    }

    public ArrayList<Chunk> getChunks() {
        return this.chunks;
    }

    public HashMap<Integer, main.java.activitytracker.Waypoint> getWps() {
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

        return str;
    }


}
