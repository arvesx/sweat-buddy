package main.java.activitytracker.server;

import main.java.activitytracker.server.fileprocessing.Chunk;
import main.java.activitytracker.server.structures.FifoQueue;
import main.java.activitytracker.server.structures.RingBuffer;

public class Utilities {
    public static final Object WORKERS_LIST_LOCK = new Object();
    public static final Object CLIENTS_LIST_LOCK = new Object();


    public static final int CHUNK_SIZE = 5;

    public static int gpxFileId = 0;
    public static final Object GXP_FILE_ID_LOCK = new Object();


    public static final FifoQueue<Chunk> messageQueue = new FifoQueue<>();
    public static final Object MESSAGE_Q_LOCK = new Object();

    public static final RingBuffer<WorkerHandlerThread> workersRingBuffer = new RingBuffer<>();
    public static final Object WORKERS_RING_BUFFER_LOCK = new Object();

}
