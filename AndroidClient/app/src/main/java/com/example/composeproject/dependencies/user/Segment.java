package com.example.composeproject.dependencies.user;

import com.example.composeproject.dependencies.fileprocessing.gpx.WaypointImpl;

import java.util.ArrayList;

public class Segment {
    public int segmentId;
    public String segmentName;
    public int segmentType;
    public ArrayList<SegmentEntry> leaderboard;
    public ArrayList<WaypointImpl> waypoints;


}
