package server;

import dependencies.fileprocessing.gpx.Chunk;
import dependencies.mapper.Map;
import dependencies.structures.FifoQueue;
import dependencies.structures.RingBuffer;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    
    public static final Object WORKERS_LIST_LOCK = new Object();
    public static final Object CLIENTS_LIST_LOCK = new Object();

    public static int gpxFileId = 0;
    public static final Object GXP_FILE_ID_LOCK = new Object();


    public static final HashMap<Integer, ArrayList<Map.WorkerResult>> intermediateResults = new HashMap<>();
    public static final Object INTERMEDIATE_RESULTS_LOCK = new Object();


    public static final FifoQueue<Chunk> messageQueue = new FifoQueue<>();
    public static final Object MESSAGE_Q_LOCK = new Object();

    public static final RingBuffer<WorkerHandlerThread> workersRingBuffer = new RingBuffer<>();
    public static final Object WORKERS_RING_BUFFER_LOCK = new Object();

}
