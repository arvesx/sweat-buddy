package main.java.activitytracker.worker;

import main.java.activitytracker.fileprocessing.gpx.Chunk;
import main.java.activitytracker.structures.FifoQueue;

class ChunkProcessorThread extends Thread {

    private boolean processingChunks;
    private final FifoQueue<Chunk> messageQueueIn;
    private final Object MSG_Q_IN_LOCK;

    private final FifoQueue<Map.WorkerResult> messageQueueOut;
    private final Object MSG_Q_OUT_LOCK;

    public ChunkProcessorThread(FifoQueue<Chunk> messageQueueIn, Object MSG_Q_IN_LOCK, FifoQueue<Map.WorkerResult> messageQueueOut, Object MSG_Q_OUT_LOCK) {
        this.processingChunks = true;
        this.messageQueueIn = messageQueueIn;
        this.MSG_Q_IN_LOCK = MSG_Q_IN_LOCK;
        this.messageQueueOut = messageQueueOut;
        this.MSG_Q_OUT_LOCK = MSG_Q_OUT_LOCK;
    }

    @Override
    public void run() {
        while (processingChunks) {
            Chunk chunkToProcess;
            synchronized (MSG_Q_IN_LOCK) {
                while (messageQueueIn.isEmpty()) {
                    try {
                        MSG_Q_IN_LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                chunkToProcess = messageQueueIn.dequeue();
            }

            // process the chunk
            Map.WorkerResult result = Map.map(chunkToProcess);
            System.out.println(result + "\n");
            synchronized (MSG_Q_OUT_LOCK) {
                messageQueueOut.enqueue(result);
                MSG_Q_OUT_LOCK.notify();
            }
        }
    }
}