package server;

import dependencies.fileprocessing.gpx.Chunk;
import dependencies.mapper.Map;
import dependencies.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static server.Utils.INTERMEDIATE_RESULTS_LOCK;
import static server.Utils.intermediateResults;

public class WorkerHandlerThread extends Thread {

    private final Socket workerSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean running;
    private boolean receivingResults;
    private final FifoQueue<Chunk> workerMessageQueue;
    private final Object MSG_Q_LOCK = new Object();

    public WorkerHandlerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
        workerMessageQueue = new FifoQueue<>();
        running = true;
        receivingResults = true;
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(workerSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(workerSocket.getInputStream());

            SenderThread sender = new SenderThread();
            sender.start();

            ResultReceiverThread receiverThread = new ResultReceiverThread();
            receiverThread.start();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private class ResultReceiverThread extends Thread {
        @Override
        public void run() {
            while (receivingResults) {
                try {
                    Map.WorkerResult workerResult = (Map.WorkerResult) inputStream.readObject();
                    int comingFromGpxFileId = workerResult.key().gpxFileId();
                    synchronized (INTERMEDIATE_RESULTS_LOCK) {
                        intermediateResults.get(comingFromGpxFileId).add(workerResult);
                        INTERMEDIATE_RESULTS_LOCK.notifyAll();
                    }

                } catch (IOException | ClassNotFoundException e) {
                    removeServerWorkerConnection();
                    synchronized(Utils.NUM_OF_WORKERS_LOCK)
                    {
                        Utils.NUM_OF_WORKERS_LOCK.notifyAll();   
                    }
                    
                    Utils.NUM_OF_WORKERS--;
                    System.out.println("NumOfWorkers: " + Utils.NUM_OF_WORKERS);
                    break;
                }
            }
        }
    }

    private class SenderThread extends Thread {

        @Override
        public void run() {
            while (running) {

                synchronized (MSG_Q_LOCK) {
                    while (workerMessageQueue.isEmpty()) {
                        try {
                            MSG_Q_LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Chunk nextChunk = workerMessageQueue.dequeue();
                    try {
                        outputStream.writeObject(nextChunk);
                        outputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    public void removeServerWorkerConnection() {
        synchronized(Utils.WORKERS_RING_BUFFER_LOCK) {
            Utils.workersRingBuffer.remove(this);
        }
    }

    public void assign(Chunk chunk) {
        synchronized (MSG_Q_LOCK) {
            workerMessageQueue.enqueue(chunk);
            MSG_Q_LOCK.notify();
        }

    }
}
