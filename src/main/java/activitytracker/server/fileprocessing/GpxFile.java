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
    private HashMap<Integer, main.java.activitytracker.Waypoint> wps;



    private ArrayList<Chunk> chunks;

    public GpxFile(String file_name) {
        this.initVariables();
        this.initGpxObject(file_name);
        this.chunks = new ArrayList<Chunk>();
    }

    private void initVariables() {
        this.wps = new HashMap<Integer, main.java.activitytracker.Waypoint>();
    }


    public void makeChunks() {

        int chunkId = 0;
        for (int i = 0; i < wps.size(); i += CHUNK_SIZE) {
            Chunk chunk = new Chunk(chunkId, this.gpxFileId);
            for (int j = 0; j < CHUNK_SIZE; j++) {
                if (i + j > wps.size() - 1) { break; }

                chunk.addData(i + j, this.wps.get(i + j));
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

            this.wps.put(i, new main.java.activitytracker.Waypoint(wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

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
        String str = "";
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
