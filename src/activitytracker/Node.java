package activitytracker;

public abstract class Node {
    
    protected int port;
    protected String ipAddress;

    public Node(int port)
    {
        this.ipAddress = "";
        this.port = port;
    }

    public Node(String ip_address, int port)
    {
        this.ipAddress = ip_address;
        this.port = port;
    }

    protected abstract void init();
    protected abstract void connect();
    protected abstract void disconnect();

}
