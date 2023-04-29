package dependencies.fileprocessing.gpx;

import java.io.Serializable;

public record GpxResults(double distanceInKilometers,
                         double totalAscentInMete,
                         double totalTimeInMinutes,
                         double avgSpeedInKilometersPerHour) implements Serializable {


}
