package com.example.composeproject.dependencies.fileprocessing;

import com.example.composeproject.dependencies.fileprocessing.gpx.GpxResults;
import com.example.composeproject.dependencies.user.GenericStats;
import com.example.composeproject.dependencies.user.UserData;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;
    public UserData userData;
    public GpxResults gpxResults;

    public GenericStats genericStats;

    public String username;
    public String password;

    public String message;
    public int success;
}
