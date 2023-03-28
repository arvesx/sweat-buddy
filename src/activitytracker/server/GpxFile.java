package activitytracker.server;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

public class GpxFile implements Serializable{

    private HashMap<Integer, Character> wpNames;

    private HashMap<Character, activitytracker.Waypoint> wps;

    public GpxFile(String file_name) {
        this.initVariables();
        this.initWpNames(); // Waypoint A, Waypoint B, ...., Waypoint Z
        this.initGpxObject(file_name);
    }

    private void initVariables() {
        this.wps = new HashMap<Character, activitytracker.Waypoint>();
        this.wpNames = new HashMap<Integer, Character>();
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
            
            this.wps.put(this.wpNames.get(i), new activitytracker.Waypoint(wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

            ++i;
        }
    }



    private void initWpNames() {
        wpNames = new HashMap<Integer, Character>();
        for (int i = 0; i < 26; i++) {
            wpNames.put(i, (char) (i + 'A'));
        }
    }

    public HashMap<Character, activitytracker.Waypoint> getWps() {
        return this.wps;
    }

    @Override
    public String toString() {
        String str = "";
        for (Character ch : this.wps.keySet()) {
            str += "Waypoint " + ch + "\n";
            str += "Latitude: " + this.wps.get(ch).getLatitude() + "\n";
            str += "Longitude: " + this.wps.get(ch).getLongitude() + "\n";
            str += "Elevation: " + this.wps.get(ch).getElevation() + "\n";
            str += "Time: " + this.wps.get(ch).getTime() + "\n";
            str += "-----------------------------------------------------" + "\n";

        }

        return str;
    }


}
