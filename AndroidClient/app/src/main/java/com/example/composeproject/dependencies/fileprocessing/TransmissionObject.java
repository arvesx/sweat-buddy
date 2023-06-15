package com.example.composeproject.dependencies.fileprocessing;

import com.example.composeproject.dependencies.fileprocessing.gpx.GpxResults;
import com.example.composeproject.dependencies.user.GenericStats;
import com.example.composeproject.dependencies.user.UserData;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import kotlin.Pair;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;
    public UserData userData;
    public List<Pair<Double, Double>> coordinates;
    public List<UserData> leaderboardList;

    public GpxResults gpxResults;

    public GenericStats genericStats;

    public String username;
    public String password;
    public String message;
    public int success;
}
