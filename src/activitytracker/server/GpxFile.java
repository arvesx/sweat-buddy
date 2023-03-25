package activitytracker.server;

import java.io.FileInputStream;
import java.util.HashMap;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

public class GpxFile {

    private HashMap<Integer, Character> wpNames;
    private GPXParser p;
    private GPX gpx;

    private HashMap<Character, Waypoint> wps;
    private HashMap<Character, Long> wpTime; // = new HashMap<Character, Long>();


    public GpxFile(String file_name) {
        this.initVariables();
        this.initWpNames(); // Waypoint A, Waypoint B, ...., Waypoint Z
        this.initGpxObject(file_name);
    }

    private void initVariables() {
        this.wps = new HashMap<Character, Waypoint>();
        this.wpTime = new HashMap<Character, Long>();
        this.wpNames = new HashMap<Integer, Character>();
        this.p = null;
        this.gpx = null;
    }



    private void initGpxObject(String file_name) {
        FileInputStream in = null;

        this.p = new GPXParser();

        try {
            in = new FileInputStream(file_name);
        } catch (Exception e) {
            System.err.println("ERROR: File_Input");
        }

        try {
            this.gpx = p.parseGPX(in);
        } catch (Exception e) {
            System.err.println("ERROR: GPX_Parse");
        }

        int i = 0;
        for (Waypoint wp : this.gpx.getWaypoints()) {
            this.wps.put(this.wpNames.get(i), wp);
            this.wpTime.put(this.wpNames.get(i), wp.getTime().getTime());

            ++i;
        }
    }



    private void initWpNames() {
        wpNames = new HashMap<Integer, Character>();
        for (int i = 0; i < 26; i++) {
            wpNames.put(i, (char) (i + 'A'));
        }
    }



    public GPX getGPX() {
        return this.gpx;
    }



    public HashMap<Character, Waypoint> getWps() {
        return this.wps;
    }



    public HashMap<Character, Long> getWpTime() {
        return this.wpTime;
    }



    @Override
    public String toString() {
        String str = "";
        for (Character ch : this.wps.keySet()) {
            str += "Waypoint " + ch + "\n";
            str += "Latitude: " + this.wps.get(ch).getLatitude() + "\n";
            str += "Longitude: " + this.wps.get(ch).getLongitude() + "\n";
            str += "Elevation: " + this.wps.get(ch).getElevation() + "\n";
            str += "Time: " + this.wpTime.get(ch) + "\n";
            str += "-----------------------------------------------------" + "\n";

        }

        return str;
    }


}
