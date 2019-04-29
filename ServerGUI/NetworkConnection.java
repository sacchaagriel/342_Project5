import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();   //new thread        //move this
    private Consumer<Serializable> callback;    //turns things to bytes     //move this

    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true); //not a user thread, a background thread
    }

    public void startConn() throws Exception{
        connthread.start();
    }

    public void send(Serializable data) throws Exception{
        connthread.out.writeObject(data);
    }

    public void closeConn() throws Exception{
        connthread.socket.close();
    }

    abstract protected String getIP();
    abstract protected int getPort();

    class ConnThread extends Thread{
        private Socket socket;  //creates a socket
        private ObjectOutputStream out;

        public void run() {
            try(ServerSocket server = new ServerSocket(getPort());
                Socket socket = server.accept();
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                }

            }
            catch(Exception e) {
                callback.accept("connection Closed");
            }
        }
    }
}
