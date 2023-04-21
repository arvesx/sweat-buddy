package main.java.activitytracker.server;

import main.java.activitytracker.fileprocessing.gpx.Chunk;
import main.java.activitytracker.worker.Map;
import main.java.activitytracker.structures.FifoQueue;
import main.java.activitytracker.structures.RingBuffer;

import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {
    public static final Object WORKERS_LIST_LOCK = new Object();
    public static final Object CLIENTS_LIST_LOCK = new Object();


    public static final int CHUNK_SIZE = 5;

    public static int gpxFileId = 0;
    public static final Object GXP_FILE_ID_LOCK = new Object();


    public static final HashMap<Integer, ArrayList<Map.WorkerResult>> intermediateResults = new HashMap<>();
    public static final Object INTERMEDIATE_RESULTS_LOCK = new Object();


    public static final FifoQueue<Chunk> messageQueue = new FifoQueue<>();
    public static final Object MESSAGE_Q_LOCK = new Object();

    public static final RingBuffer<WorkerHandlerThread> workersRingBuffer = new RingBuffer<>();
    public static final Object WORKERS_RING_BUFFER_LOCK = new Object();

}
