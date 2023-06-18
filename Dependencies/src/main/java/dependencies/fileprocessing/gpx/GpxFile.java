package dependencies.fileprocessing.gpx;

import java.io.*;
import java.util.ArrayList;

import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;
import org.alternativevision.gpx.GPXParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class GpxFile implements Serializable {

    public static final int CHUNK_SIZE = 5;

    private int gpxFileId;
    private ArrayList<WaypointImpl> wps;


    private final ArrayList<Chunk> chunks;

    public GpxFile(ArrayList<WaypointImpl> wps) {
        this.wps = wps;
        this.chunks = new ArrayList<>();
    }

    public GpxFile(String file_name) {
        this.initVariables();
        this.initGpxObject(file_name);
        this.chunks = new ArrayList<>();
    }

    public GpxFile(InputStream gpxFileInputStream) {
        this.initVariables();
        this.initGpxObjectFromStream(gpxFileInputStream);
        this.chunks = new ArrayList<>();
    }

    private void initVariables() {
        this.wps = new ArrayList<>();
    }


    public void makeChunks() {

        int chunkId = 0;
        for (int i = 0; i < wps.size(); i += CHUNK_SIZE) {

            Chunk chunk = new Chunk(chunkId, this.gpxFileId);
            for (int j = 0; j < CHUNK_SIZE; j++) {
                if (i + j > wps.size() - 1) {
                    break;
                }
                chunk.addData(this.wps.get(i + j));
            }
            this.chunks.add(chunk);
            chunkId++;
        }
    }

    private void initGpxObjectFromStream(InputStream gpxInputStream) {


        GPXParser p = new GPXParser();
        GPX gpx = null;

        try {
            gpx = p.parseGPX(gpxInputStream);
        } catch (Exception e) {
            System.err.println("ERROR: GPX_Parse");
        }

        ArrayList<WaypointImpl> temporaryList = new ArrayList<>();

        int i = 0;
        assert gpx != null;
        for (Waypoint wp : gpx.getWaypoints()) {

            temporaryList.add(new WaypointImpl(wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

            ++i;
        }

        temporaryList.sort(new WaypointImplTimeComparator());
        int id = 0;
        for (var wp : temporaryList) {
            wp.setID(id);
            id++;
        }

        this.wps = temporaryList;
    }

    private void initGpxObject(String file_name) {
        FileInputStream in = null;

        GPXParser p = new GPXParser();
        GPX gpx = null;

        try {
            in = new FileInputStream(file_name);
        } catch (Exception e) {
            System.err.println("ERROR: File_Input");
        }

        try {
            gpx = p.parseGPX(in);
        } catch (Exception e) {
            System.err.println("ERROR: GPX_Parse");
        }

        ArrayList<WaypointImpl> temporaryList = new ArrayList<>();

        int i = 0;
        assert gpx != null;
        for (Waypoint wp : gpx.getWaypoints()) {

            temporaryList.add(new WaypointImpl(wp.getLongitude(), wp.getLatitude(), wp.getElevation(), wp.getTime().getTime()));

            ++i;
        }

        temporaryList.sort(new WaypointImplTimeComparator());
        int id = 0;
        for (var wp : temporaryList) {
            wp.setID(id);
            id++;
        }

        this.wps = temporaryList;
    }

    public ArrayList<Chunk> getChunks() {
        return this.chunks;
    }

    public ArrayList<WaypointImpl> getWps() {
        return this.wps;
    }

    public int getGpxFileId() {
        return gpxFileId;
    }

    public void setGpxFileId(int gpxFileId) {
        this.gpxFileId = gpxFileId;
    }

    @Override
    public String toString() {

        StringBuilder strBuilder = new StringBuilder();
        for (WaypointImpl wp : this.wps) {
            strBuilder.append("Waypoint ").append(wp.getID()).append("\n")
                    .append("Latitude: ").append(wp.getLatitude()).append("\n")
                    .append("Longitude: ").append(wp.getLongitude()).append("\n")
                    .append("Elevation: ").append(wp.getElevation()).append("\n")
                    .append("Time: ").append(wp.getTime()).append("\n")
                    .append("-----------------------------------------------------" + "\n");

        }

        return strBuilder.toString();
    }


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        GPXParser gpxParser = new GPXParser();

        String input = """
                <?xml version="1.0"?>
                <gpx version="1.1" creator="user1">
                <wpt lat="37.95001155239993" lon="23.69503479744284">
                    <ele>12.08</ele>
                    <time>2023-03-19T17:36:01Z</time>
                </wpt>
                <wpt lat="37.949571620793144" lon="23.695699985278534">
                    <ele>9.98</ele>
                    <time>2023-03-19T17:36:31Z</time>
                </wpt>
                <wpt lat="37.94923321007226" lon="23.696236427081512">
                    <ele>8.90</ele>
                    <time>2023-03-19T17:36:55Z</time>
                </wpt>
                <wpt lat="37.948674828974916" lon="23.69680505539267">
                    <ele>9.02</ele>
                    <time>2023-03-19T17:37:26Z</time>
                </wpt>
                <wpt lat="37.94788801204387" lon="23.69558196808188">
                    <ele>6.58</ele>
                    <time>2023-03-19T17:38:21Z</time>
                </wpt>
                <wpt lat="37.94715194986727" lon="23.694605644000458">
                    <ele>4.69</ele>
                    <time>2023-03-19T17:39:08Z</time>
                </wpt>
                <wpt lat="37.946653163250694" lon="23.695273887184726">
                    <ele>5.46</ele>
                    <time>2023-03-19T17:39:40Z</time>
                </wpt>
                <wpt lat="37.94606938059409" lon="23.696046363381015">
                    <ele>5.77</ele>
                    <time>2023-03-19T17:40:17Z</time>
                </wpt>
                <wpt lat="37.94524023203743" lon="23.695102225807773">
                    <ele>4.37</ele>
                    <time>2023-03-19T17:41:06Z</time>
                </wpt>
                <wpt lat="37.944461839163594" lon="23.694050799873935">
                    <ele>3.80</ele>
                    <time>2023-03-19T17:41:56Z</time>
                </wpt>
                <wpt lat="37.943697486759284" lon="23.69324249390282">
                    <ele>2.53</ele>
                    <time>2023-03-19T17:42:40Z</time>
                </wpt>
                <wpt lat="37.943405584269506" lon="23.69371456268944">
                    <ele>2.34</ele>
                    <time>2023-03-19T17:43:01Z</time>
                </wpt>
                <wpt lat="37.943189829510054" lon="23.694036427771227">
                    <ele>2.28</ele>
                    <time>2023-03-19T17:43:15Z</time>
                </wpt>
                <wpt lat="37.942627171961284" lon="23.694884005819933">
                    <ele>2.40</ele>
                    <time>2023-03-19T17:43:53Z</time>
                </wpt>
                <wpt lat="37.94196891990529" lon="23.695816839239242">
                    <ele>2.56</ele>
                    <time>2023-03-19T17:44:36Z</time>
                </wpt>
                <wpt lat="37.941774313796756" lon="23.695747101804855">
                    <ele>3.57</ele>
                    <time>2023-03-19T17:44:44Z</time>
                </wpt>
                <wpt lat="37.941499326025664" lon="23.695532525083664">
                    <ele>3.08</ele>
                    <time>2023-03-19T17:44:58Z</time>
                </wpt>
                <wpt lat="37.94130471867361" lon="23.695323312780502">
                    <ele>2.80</ele>
                    <time>2023-03-19T17:45:09Z</time>
                </wpt>
                <wpt lat="37.94097984389345" lon="23.694886713748932">
                    <ele>2.67</ele>
                    <time>2023-03-19T17:45:30Z</time>
                </wpt>
                <wpt lat="37.94045524527583" lon="23.69421079707718">
                    <ele>2.62</ele>
                    <time>2023-03-19T17:46:03Z</time>
                </wpt>
                <wpt lat="37.94014217657466" lon="23.693808465724945">
                    <ele>2.68</ele>
                    <time>2023-03-19T17:46:22Z</time>
                </wpt>
                <wpt lat="37.93978679967615" lon="23.693347125774384">
                    <ele>2.67</ele>
                    <time>2023-03-19T17:46:44Z</time>
                </wpt>
                <wpt lat="37.93952161016134" lon="23.69299274828799">
                    <ele>3.70</ele>
                    <time>2023-03-19T17:47:01Z</time>
                </wpt>
                <wpt lat="37.93923603716365" lon="23.692619921234918">
                    <ele>5.81</ele>
                    <time>2023-03-19T17:47:19Z</time>
                </wpt>
                <wpt lat="37.9387847281908" lon="23.692015802071865">
                    <ele>4.86</ele>
                    <time>2023-03-19T17:47:48Z</time>
                </wpt>
                <wpt lat="37.93887357378787" lon="23.691527640031154">
                    <ele>2.31</ele>
                    <time>2023-03-19T17:48:05Z</time>
                </wpt>
                <wpt lat="37.93877203595387" lon="23.691431080506618">
                    <ele>2.09</ele>
                    <time>2023-03-19T17:48:10Z</time>
                </wpt>
                <wpt lat="37.93911472558143" lon="23.690717612908657">
                    <ele>2.47</ele>
                    <time>2023-03-19T17:48:39Z</time>
                </wpt>
                <wpt lat="37.939673179328615" lon="23.690224610251718">
                    <ele>2.92</ele>
                    <time>2023-03-19T17:49:09Z</time>
                </wpt>
                <wpt lat="37.94003701812341" lon="23.690106593055063">
                    <ele>2.97</ele>
                    <time>2023-03-19T17:49:25Z</time>
                </wpt>
                <wpt lat="37.94053200521952" lon="23.6907074078744">
                    <ele>2.16</ele>
                    <time>2023-03-19T17:49:55Z</time>
                </wpt>
                <wpt lat="37.94081968849565" lon="23.69096489993983">
                    <ele>2.25</ele>
                    <time>2023-03-19T17:50:10Z</time>
                </wpt>
                <wpt lat="37.94162423069252" lon="23.69053836283811">
                    <ele>2.57</ele>
                    <time>2023-03-19T17:50:48Z</time>
                </wpt>
                <wpt lat="37.942208048664426" lon="23.691133813239418">
                    <ele>2.89</ele>
                    <time>2023-03-19T17:51:21Z</time>
                </wpt>
                <wpt lat="37.94297477498236" lon="23.69215686087734">
                    <ele>3.00</ele>
                    <time>2023-03-19T17:52:10Z</time>
                </wpt>
                <wpt lat="37.94371087900379" lon="23.69313854937679">
                    <ele>2.58</ele>
                    <time>2023-03-19T17:52:57Z</time>
                </wpt>
                <wpt lat="37.944477889945155" lon="23.693989646908282">
                    <ele>3.79</ele>
                    <time>2023-03-19T17:53:42Z</time>
                </wpt>
                <wpt lat="37.94513360173761" lon="23.693201077457903">
                    <ele>2.99</ele>
                    <time>2023-03-19T17:54:22Z</time>
                </wpt>
                </gpx>
                                
                """;

        InputStream in = new ByteArrayInputStream(input.getBytes());
        GpxFile newFile = new GpxFile(in);
        System.out.println("hello");
        System.out.println(newFile);


    }

}
