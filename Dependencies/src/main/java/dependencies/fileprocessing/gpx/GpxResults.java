package dependencies.fileprocessing.gpx;

import java.io.Serializable;

public record GpxResults(double distanceInKilometers,
                         double totalAscentInMete,
                         double totalTimeInMinutes,
                         double avgSpeedInKilometersPerHour,
                         long totalTimeInMillis) implements Serializable {


}
