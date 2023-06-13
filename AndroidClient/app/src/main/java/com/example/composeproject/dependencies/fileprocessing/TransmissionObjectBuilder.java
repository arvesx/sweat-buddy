package com.example.composeproject.dependencies.fileprocessing;


import com.example.composeproject.dependencies.user.UserData;

import java.util.List;

import kotlin.Pair;

public class TransmissionObjectBuilder {
    private final TransmissionObject to;

    public TransmissionObjectBuilder() {
        to = new TransmissionObject();
    }

    public TransmissionObjectBuilder type(TransmissionObjectType type) {
        to.type = type;
        return this;
    }

    public TransmissionObjectBuilder userData(UserData userData) {
        to.userData = userData;
        return this;
    }

    public TransmissionObjectBuilder coordinates(List<Pair<Double, Double>> coordinates) {
        to.coordinates = coordinates;
        return this;
    }
    public TransmissionObjectBuilder username(String username) {
        to.username = username;
        return this;
    }

    public TransmissionObjectBuilder password(String password) {
        to.password = password;
        return this;
    }

    public TransmissionObjectBuilder success(int success) {
        to.success = success;
        return this;
    }

    public TransmissionObjectBuilder message(String message) {
        to.message = message;
        return this;
    }

    public TransmissionObjectBuilder userId(int userId) {
        to.userId = userId;
        return this;
    }

    public TransmissionObjectBuilder gpxFile(String gpxFile) {
        to.gpxFile = gpxFile;
        return this;
    }

    public TransmissionObject craft() {
        return to;
    }
}
