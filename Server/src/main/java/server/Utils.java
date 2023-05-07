package server;

import dependencies.fileprocessing.gpx.Chunk;
import dependencies.mapper.Map;
import dependencies.structures.FifoQueue;
import dependencies.structures.RingBuffer;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Utils {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static final int MAX_WORKERS = 5;
    public static int NUM_OF_WORKERS = 0;
    public static final Object NUM_OF_WORKERS_LOCK = new Object();

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
