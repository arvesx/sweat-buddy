package com.example.composeproject.dependencies.fileprocessing;

import com.example.composeproject.dependencies.fileprocessing.gpx.GpxResults;
import com.example.composeproject.dependencies.user.GenericStats;
import com.example.composeproject.dependencies.user.LeaderboardEntry;
import com.example.composeproject.dependencies.user.Segment;
import com.example.composeproject.dependencies.user.UserData;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;

    public String segmentFile;
    public int segmentId;
    public Segment segmentObject;

    public int segmentStart;
    public int segmentEnd;


    public int routeId;

    public UserData userData;
    public GpxResults gpxResults;

    public GenericStats genericStats;

    public List<Pair<Double, Double>> coordinates;
    public ArrayList<LeaderboardEntry> leaderboardList;


    public String username;
    public String password;

    public String message;
    public int success;
}
