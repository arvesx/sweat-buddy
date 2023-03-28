package activitytracker;

public abstract class Node {
    
    protected int port;

    public Node(int port)
    {
        this.port = port;
    }

    protected abstract void init();
    protected abstract void connect();
    protected abstract void disconnect();

}
