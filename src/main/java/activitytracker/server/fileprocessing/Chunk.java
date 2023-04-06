package main.java.activitytracker.server.fileprocessing;

import main.java.activitytracker.Waypoint;

import java.util.HashMap;

public class Chunk {

    private int fileId;
    private int chunkId;
    private HashMap<Integer, Waypoint> wps;


    public Chunk()
    {
        this.wps = new HashMap<Integer, Waypoint>();
    }
}
