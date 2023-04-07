package main.java.activitytracker.server.fileprocessing;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

public class GpxFile implements Serializable {

    private int gpxFileId;
    private HashMap<Integer, main.java.activitytracker.Waypoint> wps;

    public GpxFile(String file_name) {
        this.initVariables();
        this.initGpxObject(file_name);
    }

    private void initVariables() {
        this.wps = new HashMap<Integer, main.java.activitytracker.Waypoint>();
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


    public HashMap<Integer, main.java.activitytracker.Waypoint> getWps() {
        return this.wps;
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
