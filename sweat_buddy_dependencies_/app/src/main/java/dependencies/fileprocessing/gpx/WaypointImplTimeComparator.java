package dependencies.fileprocessing.gpx;

import java.util.Comparator;

public class WaypointImplTimeComparator implements Comparator<WaypointImpl> {
    @Override
    public int compare(WaypointImpl o1, WaypointImpl o2) {
        return Long.compare(o1.getTime(), o2.getTime());
    }
}
