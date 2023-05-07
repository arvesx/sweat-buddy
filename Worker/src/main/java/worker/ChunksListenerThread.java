package worker;

import dependencies.fileprocessing.gpx.Chunk;
import dependencies.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;

class ChunksListenerThread extends Thread {
    private boolean listeningForIncomingChunks;
    private final ObjectInputStream inputStream;

    private final FifoQueue<Chunk> messageQueueIn;

    private final Object MSG_Q_IN_LOCK;

    public ChunksListenerThread(ObjectInputStream inputStream, FifoQueue<Chunk> messageQueueIn, Object LOCK) {
        this.listeningForIncomingChunks = true;
        this.inputStream = inputStream;
        this.messageQueueIn = messageQueueIn;
        this.MSG_Q_IN_LOCK = LOCK;
    }

    @Override
    public void run() {

        while (listeningForIncomingChunks) {
            try {
                Chunk chunk = (Chunk) inputStream.readObject();
                System.out.print(chunk.getChunkId() + " ");
                synchronized (MSG_Q_IN_LOCK) {
                    messageQueueIn.enqueue(chunk);
                    MSG_Q_IN_LOCK.notifyAll();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}